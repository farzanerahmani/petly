package com.petly.food.repository;

import com.petly.food.entity.FoodVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
public interface FoodVariantRepository extends JpaRepository<FoodVariant, Long> {

    List<FoodVariant> findByFoodProductId(Long foodProductId);

    Optional<FoodVariant> findByFoodProductIdAndPackageWeightGrams(
            Long foodProductId,
            Integer packageWeightGrams
    );

    boolean existsByFoodProductIdAndPackageWeightGrams(
            Long foodProductId,
            Integer packageWeightGrams
    );

    List<FoodVariant> findByFoodProductIdAndPackageWeightGramsIsNull(
            Long foodProductId
    );

    boolean existsByFoodProductIdAndPackageWeightGramsIsNull(
            Long foodProductId
    );
}
