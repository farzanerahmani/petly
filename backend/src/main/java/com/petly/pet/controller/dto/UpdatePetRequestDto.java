package com.petly.pet.controller.dto;

import com.petly.pet.entity.enums.ActivityLevel;
import com.petly.pet.entity.enums.Gender;
import com.petly.pet.entity.enums.Species;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/3/2026
 */
@Data
public class UpdatePetRequestDto {

    private String name;
    private String breed;
    private Gender gender;
    private LocalDate birthDate;
    private ActivityLevel activityLevel;
}

