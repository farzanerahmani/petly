package com.petly.food.repository;

import com.petly.food.entity.FoodPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
public interface FoodPriceRepository extends JpaRepository<FoodPrice, Long> {

    List<FoodPrice> findAllByFoodProductIdOrderByCheckedAtDesc(Long foodProductId);
}

