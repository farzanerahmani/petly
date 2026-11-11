package com.petly.notification.dto;

import com.petly.notification.entity.UserFoodWatchlist;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/31/2026
 */
public record FoodWatchlistResponse(
        Long foodProductId,
        Instant addedAt
) {
    public static FoodWatchlistResponse from(UserFoodWatchlist item) {
        return new FoodWatchlistResponse(
                item.getFoodProductId(),
                item.getCreatedAt()
        );
    }
}

