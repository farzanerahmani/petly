package com.petly.food.repository;

import com.petly.food.entity.FoodNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
public interface FoodNutritionRepository extends JpaRepository<FoodNutrition, Long> {

    Optional<FoodNutrition> findByFoodProductId(Long foodProductId);
}
