package com.petly.food.controller;

import com.petly.food.controller.dto.FoodProductResponseDto;
import com.petly.food.entity.enums.FoodType;
import com.petly.food.service.FoodProductService;
import com.petly.pet.entity.enums.Species;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
@RestController
@RequestMapping("/api/v1/foods")
public class FoodProductController {

    private final FoodProductService foodProductService;

    public FoodProductController(FoodProductService foodProductService) {
        this.foodProductService = foodProductService;
    }

    @GetMapping
    public List<FoodProductResponseDto> getFoods(
            @RequestParam(required = false) Species species,
            @RequestParam(required = false) FoodType foodType
    ) {
        return foodProductService.getFoods(species, foodType);
    }

    @GetMapping("/{id}")
    public FoodProductResponseDto getFoodById(
            @PathVariable Long id
    ) {
        return foodProductService.getFoodById(id);
    }
}
