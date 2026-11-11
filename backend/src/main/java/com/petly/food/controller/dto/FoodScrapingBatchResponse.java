package com.petly.food.controller.dto;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
public record FoodScrapingBatchResponse(
        int totalCount,
        int successCount,
        int failureCount
) {
}

