package com.petly.food.service;

import com.petly.food.controller.dto.PetShopCreateRequest;
import com.petly.food.controller.dto.PetShopResponse;
import com.petly.food.controller.dto.PetShopUpdateRequest;
import com.petly.food.entity.PetShop;
import com.petly.food.repository.PetShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
@Service
public class PetShopService {

    private final PetShopRepository petShopRepository;

    public PetShopService(PetShopRepository petShopRepository) {
        this.petShopRepository = petShopRepository;
    }

    @Transactional
    public PetShop findOrCreatePetShop(
            String name,
            String city,
            String websiteUrl
    ) {
        return petShopRepository.findByWebsiteUrl(websiteUrl)
                .map(petShop -> {
                    if (!petShop.isActive()) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Pet shop already exists but is inactive."
                        );
                    }
                    return petShop;
                })
                .orElseGet(() -> {
                    PetShop petShop = new PetShop();
                    petShop.setName(name);
                    petShop.setCity(city);
                    petShop.setWebsiteUrl(websiteUrl);
                    petShop.setActive(true);

                    return petShopRepository.save(petShop);
                });
    }

    @Transactional(readOnly = true)
    public PetShop getActivePetShopByWebsiteUrl(String websiteUrl) {
        PetShop petShop = petShopRepository.findByWebsiteUrl(websiteUrl)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pet shop not found."
                        ));

        if (!petShop.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Pet shop is inactive.");
        }

        return petShop;
    }

    public PetShopResponse createPetShop(PetShopCreateRequest request) {

        PetShop shop = findOrCreatePetShop(
                request.getName(),
                request.getCity(),
                request.getWebsiteUrl()
        );

        return PetShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .city(shop.getCity())
                .websiteUrl(shop.getWebsiteUrl())
                .address(shop.getAddress())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .supportsDelivery(shop.isSupportsDelivery())
                .deliveryNotes(shop.getDeliveryNotes())
                .active(shop.isActive())
                .build();
    }

    @Transactional
    public PetShop updatePetShop(
            Long petShopId, PetShopUpdateRequest request
    ) {
        PetShop shop = petShopRepository.findById(petShopId)
                .orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet shop not found."
                )
        );

        if (!shop.isActive()) {
            if (!shop.isActive()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Pet shop is inactive."
                );
            }
        }

        shop.setName(request.getName().trim());
        shop.setCity(request.getCity().trim());
        shop.setWebsiteUrl(request.getWebsiteUrl().trim());
        shop.setAddress(request.getAddress());
        shop.setLatitude(request.getLatitude());
        shop.setLongitude(request.getLongitude());
        shop.setSupportsDelivery(request.getSupportsDelivery());
        shop.setDeliveryNotes(
                Boolean.TRUE.equals(request.getSupportsDelivery())
                        ? request.getDeliveryNotes()
                        : null
        );

        return petShopRepository.save(shop);
    }

}
