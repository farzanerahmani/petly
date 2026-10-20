package com.petly.pet.controller.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 *  @author farzane.rahmani
 * @created 10/17/2026
 */
@Data
public class CalorieCalculationRequest {
    private Long petId;
    private String species;
    private BigDecimal weightKg;
    private LocalDate birthDate;
    private boolean neutered;
    private String activityLevel;
}
