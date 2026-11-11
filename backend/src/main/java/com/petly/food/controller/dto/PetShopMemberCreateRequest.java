package com.petly.food.controller.dto;

import com.petly.food.entity.enums.PetShopMemberRole;
import jakarta.validation.constraints.NotBlank;
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
public class PetShopMemberCreateRequest {

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @NotNull
    private PetShopMemberRole role;
}

