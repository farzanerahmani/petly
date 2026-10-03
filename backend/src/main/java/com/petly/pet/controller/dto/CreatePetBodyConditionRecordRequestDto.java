package com.petly.pet.controller.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
@Data
public class CreatePetBodyConditionRecordRequestDto {
    private short score;
    private LocalDate recordedDate;
}
