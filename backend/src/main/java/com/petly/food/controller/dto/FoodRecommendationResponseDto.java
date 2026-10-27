package com.petly.food.controller.dto;

import com.petly.food.entity.enums.FoodLifeStage;
import com.petly.pet.entity.enums.Species;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/21/2026
 */
@Getter
@Setter
@NoArgsConstructor
public class FoodRecommendationResponseDto {

    private Long petId;
    private String petName;
    private Species species;
    private BigDecimal latestWeightKg;
    private LocalDate weightRecordedDate;
    private Short bodyConditionScore;
    private LocalDate bodyConditionRecordedDate;
    private FoodLifeStage expectedLifeStage;
    private String notice;
    private List<RecommendationItem> recommendations;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RecommendationItem {
        private FoodProductResponseDto food;
        private int score;
        private List<String> reasons;
        private List<String> warnings;
    }
}


