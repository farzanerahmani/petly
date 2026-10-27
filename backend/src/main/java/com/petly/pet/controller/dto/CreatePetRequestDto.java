package com.petly.pet.controller.dto;

import com.petly.pet.entity.enums.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 9/29/2026
 */
@Data
public class CreatePetRequestDto {
    private String name;
    private Species species;
    private String breed;
    private Gender gender;
    private LocalDate birthDate;
    private ActivityLevel activityLevel;
    private Boolean neutered;
    private FoodTypePreference foodTypePreference;
    private String currentFood;
    private CurrentFoodSatisfaction currentFoodSatisfaction;
}
