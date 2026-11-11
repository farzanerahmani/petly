
package com.petly.food.service;

import com.petly.food.controller.dto.StoreOwnerRequestCreateRequest;
import com.petly.food.controller.dto.StoreOwnerRequestResponse;
import com.petly.food.entity.StoreOwnerRequest;
import com.petly.food.entity.enums.StoreOwnerRequestStatus;
import com.petly.food.repository.StoreOwnerRequestRepository;
import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 11/5/2026
 */
@Service
@RequiredArgsConstructor
public class StoreOwnerRequestService {

    private final StoreOwnerRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreOwnerRequestResponse createRequest(
            Long userId,
            StoreOwnerRequestCreateRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        String shopName = request.getShopName().trim();
        String city = request.getCity().trim();

        boolean duplicate = requestRepository
                .existsByUserIdAndShopNameIgnoreCaseAndCityIgnoreCaseAndStatus(
                        userId,
                        shopName,
                        city,
                        StoreOwnerRequestStatus.PENDING
                );

        if (duplicate) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You already have a pending request for this shop"
            );
        }

        StoreOwnerRequest entity = new StoreOwnerRequest();
        entity.setUser(user);
        entity.setShopName(shopName);
        entity.setCity(city);
        entity.setWebsiteUrl(normalizeOptional(request.getWebsiteUrl()));
        entity.setAddress(normalizeOptional(request.getAddress()));
        entity.setStatus(StoreOwnerRequestStatus.PENDING);

        try {
            StoreOwnerRequest saved = requestRepository.saveAndFlush(entity);
            return toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A pending request for this store already exists"
            );
        }
    }

    @Transactional(readOnly = true)
    public List<StoreOwnerRequestResponse> getMyRequests(Long userId) {
        return requestRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private StoreOwnerRequestResponse toResponse(
            StoreOwnerRequest entity
    ) {
        return new StoreOwnerRequestResponse(
                entity.getId(),
                entity.getShopName(),
                entity.getCity(),
                entity.getWebsiteUrl(),
                entity.getAddress(),
                entity.getStatus(),
                entity.getPetShop() != null
                        ? entity.getPetShop().getId()
                        : null,
                entity.getRejectionReason(),
                entity.getCreatedAt(),
                entity.getReviewedAt()
        );
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

