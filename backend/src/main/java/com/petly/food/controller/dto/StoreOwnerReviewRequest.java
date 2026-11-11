package com.petly.food.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 11/8/2026
 */
@Getter
@Setter
public class StoreOwnerReviewRequest {

    @NotNull
    private Boolean approved;

    private Long petShopId;

    @Size(max = 2000)
    private String rejectionReason;
}

