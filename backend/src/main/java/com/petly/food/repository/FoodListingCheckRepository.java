package com.petly.food.repository;


import com.petly.food.entity.FoodListingCheck;
import com.petly.food.entity.enums.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */


public interface FoodListingCheckRepository
        extends JpaRepository<FoodListingCheck, Long> {

    List<FoodListingCheck> findByFoodListingIdOrderByCheckedAtDesc(
            Long foodListingId
    );

    Optional<FoodListingCheck> findTopByFoodListingIdOrderByCheckedAtDesc(
            Long foodListingId
    );

    List<FoodListingCheck> findByAvailabilityOrderByCheckedAtDesc(
            AvailabilityStatus availability
    );
}
