package com.petly.food.controller.dto;

import com.petly.food.entity.enums.PetShopMemberRole;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 11/8/2026
 */
public record PetShopMemberResponse(
        Long id,
        Long userId,
        String phoneNumber,
        PetShopMemberRole role,
        boolean active,
        Instant createdAt
) {
}

