package com.petly.food.repository;

import com.petly.food.entity.FoodMatchCandidate;
import com.petly.food.entity.enums.MatchCandidateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
public interface FoodMatchCandidateRepository extends JpaRepository<FoodMatchCandidate, Long> {

    List<FoodMatchCandidate> findByFoodListingId(Long foodListingId);

    List<FoodMatchCandidate> findByFoodListingIdAndStatusOrderByConfidenceScoreDesc(
            Long foodListingId,
            MatchCandidateStatus status
    );

    List<FoodMatchCandidate> findByFoodVariantId(Long foodVariantId);

    Optional<FoodMatchCandidate> findByFoodListingIdAndFoodVariantId(
            Long foodListingId,
            Long foodVariantId
    );

    boolean existsByFoodListingIdAndFoodVariantId(
            Long foodListingId,
            Long foodVariantId
    );

    List<FoodMatchCandidate>
    findByStatusOrderByCreatedAtDesc(
            MatchCandidateStatus status
    );
}