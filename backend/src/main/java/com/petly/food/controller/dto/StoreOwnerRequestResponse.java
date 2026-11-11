package com.petly.food.controller.dto;

import com.petly.food.entity.enums.StoreOwnerRequestStatus;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 11/5/2026
 */
public record StoreOwnerRequestResponse(
        Long id,
        String shopName,
        String city,
        String websiteUrl,
        String address,
        StoreOwnerRequestStatus status,
        Long petShopId,
        String rejectionReason,
        Instant createdAt,
        Instant reviewedAt
) {
}

