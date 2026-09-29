package com.petly.pet.controller.dto;

import lombok.Data;

/**
 * @author farzane.rahmani
 * @created 9/29/2026
 */
@Data
public class CreatePetResponseDto {
    private Long id;
    private String name;
    private String species;
}
