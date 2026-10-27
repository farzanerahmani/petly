package com.petly.food.repository;

import com.petly.food.entity.FoodProduct;
import com.petly.food.entity.enums.FoodType;
import com.petly.pet.entity.enums.Species;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
public interface FoodProductRepository extends JpaRepository<FoodProduct, Long> {

    List<FoodProduct> findAllByActiveTrueOrderByNameAsc();

    List<FoodProduct> findAllBySpeciesAndActiveTrueOrderByNameAsc(Species species);

    List<FoodProduct> findAllBySpeciesAndFoodTypeAndActiveTrueOrderByNameAsc(Species species, FoodType foodType);

    List<FoodProduct> findAllByFoodTypeAndActiveTrueOrderByNameAsc(FoodType foodType);
}

