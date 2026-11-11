package com.petly.notification.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author farzane.rahmani
 * @created 10/31/2026
 */
public record AddFoodWatchlistRequest(
        @NotNull
        @Positive
        Long foodProductId
) {
}

