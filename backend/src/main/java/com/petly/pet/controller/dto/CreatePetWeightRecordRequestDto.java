package com.petly.pet.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
@Data
public class CreatePetWeightRecordRequestDto {

    private BigDecimal weightKg;
    private LocalDate recordedDate;

}
