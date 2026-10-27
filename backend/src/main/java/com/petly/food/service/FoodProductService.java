package com.petly.food.service;

import com.petly.common.exception.FoodNotFoundException;
import com.petly.food.controller.dto.FoodProductResponseDto;
import com.petly.food.entity.FoodProduct;
import com.petly.food.entity.enums.FoodType;
import com.petly.food.repository.FoodProductRepository;
import com.petly.pet.entity.enums.Species;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
@Service
public class FoodProductService {

    private final FoodProductRepository foodProductRepository;

    public FoodProductService(
            FoodProductRepository foodProductRepository
    ) {
        this.foodProductRepository = foodProductRepository;
    }

    @Transactional
    public List<FoodProductResponseDto> getFoods(
            Species species,
            FoodType foodType
    ) {
        List<FoodProduct> products;

        if (species != null && foodType != null) {
            products = foodProductRepository
                    .findAllBySpeciesAndFoodTypeAndActiveTrueOrderByNameAsc(
                            species,
                            foodType
                    );
        } else if (species != null) {
            products = foodProductRepository
                    .findAllBySpeciesAndActiveTrueOrderByNameAsc(species);
        } else if (foodType != null) {
            products = foodProductRepository
                    .findAllByFoodTypeAndActiveTrueOrderByNameAsc(foodType);
        } else {
            products = foodProductRepository
                    .findAllByActiveTrueOrderByNameAsc();
        }

        return products.stream()
                .map(FoodProductResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public FoodProductResponseDto getFoodById(Long id) {
        FoodProduct product = foodProductRepository.findById(id)
                .filter(FoodProduct::isActive)
                .orElseThrow(() ->
                        new FoodNotFoundException(
                                "Food product not found: " + id
                        )
                );

        return FoodProductResponseDto.fromEntity(product);
    }
}
