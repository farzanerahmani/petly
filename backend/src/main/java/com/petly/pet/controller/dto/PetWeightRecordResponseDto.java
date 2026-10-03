package com.petly.pet.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
@Data
public class PetWeightRecordResponseDto {
    private Long id;
    private Long petId;
    private BigDecimal weightKg;
    private LocalDate recordedDate;
    private Instant createdAt;

}
