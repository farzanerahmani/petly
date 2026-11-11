package com.petly.food.service;

import com.petly.food.controller.dto.FoodScrapingRequest;
import com.petly.food.entity.FoodListing;
import com.petly.food.entity.FoodListingCheck;
import com.petly.food.entity.FoodPrice;
import com.petly.food.entity.PetShop;
import com.petly.food.entity.enums.AvailabilityStatus;
import com.petly.food.repository.FoodListingCheckRepository;
import com.petly.food.repository.FoodListingRepository;
import com.petly.food.repository.FoodPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
@Service
public class FoodScrapingItemService {

    private final FoodListingRepository listingRepository;
    private final FoodListingCheckRepository checkRepository;
    private final FoodPriceRepository priceRepository;

    public FoodScrapingItemService(
            FoodListingRepository listingRepository,
            FoodListingCheckRepository checkRepository,
            FoodPriceRepository priceRepository
    ) {
        this.listingRepository = listingRepository;
        this.checkRepository = checkRepository;
        this.priceRepository = priceRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(
            PetShop petShop,
            FoodScrapingRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required.");
        }

        if (petShop == null || petShop.getId() == null) {
            throw new IllegalArgumentException("Pet shop is required.");
        }

        if (request.getProductUrl() == null
                || request.getProductUrl().isBlank()) {
            throw new IllegalArgumentException("Product URL is required.");
        }

        Instant checkedAt = request.getCheckedAt() != null
                ? request.getCheckedAt()
                : Instant.now();

        String productUrl = request.getProductUrl().trim();

        String productName = request.getProductName() == null
                ? null
                : request.getProductName().trim();

        boolean scrapeFailed = request.getScrapeError() != null
                && !request.getScrapeError().isBlank();

        // Find existing listing.
        FoodListing listing = listingRepository
                .findByPetShopIdAndProductUrl(
                        petShop.getId(),
                        productUrl
                )
                .orElse(null);

        // Create a new listing only when its name is available.
        if (listing == null) {
            if (productName == null || productName.isBlank()) {
                throw new IllegalArgumentException(
                        "Product name is required for a new listing."
                );
            }

            listing = new FoodListing();
            listing.setPetShop(petShop);
            listing.setProductUrl(productUrl);
            listing.setRawProductName(productName);
            listing.setFirstSeenAt(checkedAt);

        } else if (productName != null && !productName.isBlank()) {
            listing.setRawProductName(productName);
        }

        listing.setLastSeenAt(checkedAt);
        listing = listingRepository.save(listing);

        // Save every scraper observation, including failed observations.
        FoodListingCheck check = new FoodListingCheck();
        check.setFoodListing(listing);

        check.setAvailability(
                scrapeFailed || request.getAvailability() == null
                        ? AvailabilityStatus.UNKNOWN
                        : request.getAvailability()
        );

        check.setStockText(request.getStockText());
        check.setScrapeError(request.getScrapeError());
        check.setCheckedAt(checkedAt);

        check = checkRepository.save(check);

        // Failed scraping must never create a price observation.
        if (scrapeFailed) {
            return;
        }

        BigDecimal priceAmount = parsePrice(
                request.getPriceAmountDisplayed()
        );

        // Missing or invalid price means no price record.
        if (priceAmount == null
                || request.getDisplayedCurrency() == null) {
            return;
        }

        // Create a new historical price observation.
        FoodPrice price = new FoodPrice();

        price.setFoodListing(listing);
        price.setListingCheckId(check.getId());
        price.setPetShop(petShop);
        price.setSellerName(petShop.getName());
        price.setProductUrl(productUrl);

        price.setPriceAmount(priceAmount);
        price.setCurrency(request.getDisplayedCurrency());

        // Map availability independently from price.
        price.setAvailable(
                toNullableBoolean(check.getAvailability())
        );

        // The stock check was attempted successfully,
        // even when the resulting availability is UNKNOWN.
        price.setStockCheckedAt(checkedAt);

        price.setCheckedAt(checkedAt);

        // Last successful price observation.
        price.setLastVerifiedAt(checkedAt);

        priceRepository.save(price);
    }

    private Boolean toNullableBoolean(AvailabilityStatus availability) {
        if (availability == null
                || availability == AvailabilityStatus.UNKNOWN) {
            return null;
        }

        return availability == AvailabilityStatus.IN_STOCK;
    }

    private BigDecimal parsePrice(String rawPrice) {
        if (rawPrice == null || rawPrice.isBlank()) {
            return null;
        }

        String normalized = normalizeDigits(rawPrice)
                .replace(",", "")
                .replace("٬", "")
                .replace("٫", ".")
                .replaceAll("[^0-9.]", "")
                .trim();

        if (normalized.isBlank()) {
            return null;
        }

        try {
            BigDecimal price = new BigDecimal(normalized);

            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                return null;
            }

            return price;

        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String normalizeDigits(String value) {
        StringBuilder result = new StringBuilder();

        for (char character : value.toCharArray()) {
            if (character >= '۰' && character <= '۹') {
                result.append((char) ('0' + character - '۰'));

            } else if (character >= '٠' && character <= '٩') {
                result.append((char) ('0' + character - '٠'));

            } else {
                result.append(character);
            }
        }

        return result.toString();
    }
}
