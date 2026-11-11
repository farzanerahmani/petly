package com.petly.food.repository;

import com.petly.food.entity.FoodListing;
import com.petly.food.entity.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
public interface FoodListingRepository extends JpaRepository<FoodListing, Long> {

    List<FoodListing> findByPetShopId(Long petShopId);

    List<FoodListing> findByPetShopIdAndActiveTrue(Long petShopId);

    List<FoodListing> findByMatchingStatus(MatchingStatus matchingStatus);

    List<FoodListing> findByPetShopIdAndMatchingStatus(
            Long petShopId,
            MatchingStatus matchingStatus
    );

    Optional<FoodListing> findByPetShopIdAndProductUrl(
            Long petShopId,
            String productUrl
    );

    List<FoodListing> findByFoodVariantId(Long foodVariantId);

    List<FoodListing> findByActiveTrueAndMatchingStatus(
            MatchingStatus matchingStatus
    );

    List<FoodListing> findByPetShopIdAndActiveTrueAndMatchingStatus(
            Long petShopId,
            MatchingStatus matchingStatus
    );
}
