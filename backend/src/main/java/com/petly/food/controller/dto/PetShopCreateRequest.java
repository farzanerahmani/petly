package com.petly.food.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
@Getter
@Setter
public class PetShopCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 500)
    private String websiteUrl;
}
