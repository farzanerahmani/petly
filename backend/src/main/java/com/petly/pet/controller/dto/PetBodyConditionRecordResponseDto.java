package com.petly.pet.controller.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
@Data
public class PetBodyConditionRecordResponseDto {
    private Long id;
    private Long petId;
    private int score;
    private LocalDate recordedDate;
    private Instant createdAt;
}
