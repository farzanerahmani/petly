package com.petly.food.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 11/5/2026
 */
@Getter
@Setter
public class StoreOwnerRequestCreateRequest {

    @NotBlank
    @Size(max = 200)
    private String shopName;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Size(max = 500)
    private String websiteUrl;

    @Size(max = 2000)
    private String address;
}

