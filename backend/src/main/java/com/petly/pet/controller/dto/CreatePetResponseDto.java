package com.petly.pet.controller.dto;

import com.petly.pet.entity.enums.ActivityLevel;
import com.petly.pet.entity.enums.Gender;
import com.petly.pet.entity.enums.Species;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 9/29/2026
 */
@Data
public class CreatePetResponseDto {
    private Long id;
    private String name;
    private Species species;
    private String breed;
    private Gender gender;
    private LocalDate birthDate;
    private ActivityLevel activityLevel;
    private Instant createdAt;
    private Instant updatedAt;;
}
