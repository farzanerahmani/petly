package com.petly.food.controller;

import com.petly.food.controller.dto.FoodRecommendationResponseDto;
import com.petly.food.service.FoodRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
@RestController
@RequestMapping("/api/v1/pets/{petId}/food-recommendations")
@RequiredArgsConstructor
public class FoodRecommendationController {

    private final FoodRecommendationService foodRecommendationService;

    @GetMapping
    public FoodRecommendationResponseDto getRecommendations(
            @PathVariable Long petId
    ) {
        return foodRecommendationService.getRecommendations(petId);
    }
}
