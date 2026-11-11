package com.petly.food.service;


import com.petly.food.controller.dto.FoodScrapingBatchResponse;
import com.petly.food.controller.dto.FoodScrapingRequest;
import com.petly.food.entity.PetShop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
@Service
public class FoodScrapingService {

    private static final Logger log =
            LoggerFactory.getLogger(FoodScrapingService.class);

    private final PetShopService petShopService;
    private final FoodScrapingItemService itemService;

    public FoodScrapingService(
            PetShopService petShopService,
            FoodScrapingItemService itemService
    ) {
        this.petShopService = petShopService;
        this.itemService = itemService;
    }

    public FoodScrapingBatchResponse processBatch(
            String shopWebsiteUrl,
            List<FoodScrapingRequest> requests
    ) {
        if (shopWebsiteUrl == null || shopWebsiteUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "Shop website URL is required."
            );
        }

        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException(
                    "Scraping request must contain at least one product."
            );
        }

        PetShop petShop =
                petShopService.getActivePetShopByWebsiteUrl(
                        shopWebsiteUrl.trim()
                );

        int successCount = 0;
        int failureCount = 0;

        for (FoodScrapingRequest request : requests) {
            try {
                itemService.process(petShop, request);
                successCount++;

            } catch (Exception e) {
                failureCount++;

                String productUrl = request == null
                        ? "UNKNOWN"
                        : request.getProductUrl();

                log.error(
                        "Failed to process scraped product. Shop ID: {}, URL: {}",
                        petShop.getId(),
                        productUrl,
                        e
                );
            }
        }

        log.info(
                "Scraping batch completed. Shop ID: {}, Total: {}, Success: {}, Failed: {}",
                petShop.getId(),
                requests.size(),
                successCount,
                failureCount
        );

        return new FoodScrapingBatchResponse(
                requests.size(),
                successCount,
                failureCount
        );
    }
}



