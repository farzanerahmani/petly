package com.petly.food.controller.dto;

import com.petly.food.entity.enums.AvailabilityStatus;
import com.petly.food.entity.enums.FoodCurrency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
@Getter
@Setter
public class FoodScrapingRequest {

    @Size(max = 500)
    private String productName;

    @NotBlank
    private String productUrl;

    private String priceAmountDisplayed;

    private FoodCurrency displayedCurrency;

    @Positive
    private Integer packageWeightGrams;

    private Boolean weightReviewRequired;

    @Size(max = 100)
    private String weightSource;

    private AvailabilityStatus availability;

    @Size(max = 500)
    private String stockText;

    private Instant checkedAt;

    private String scrapeError;
}

