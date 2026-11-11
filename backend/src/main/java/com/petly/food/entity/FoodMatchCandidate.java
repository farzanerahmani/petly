package com.petly.food.entity;

import com.petly.common.entity.AuditableEntity;
import com.petly.food.entity.enums.MatchCandidateStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "food_match_candidates",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_food_match_candidate",
                        columnNames = {
                                "food_listing_id",
                                "food_variant_id"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_food_match_candidates_status",
                        columnList = "status, created_at DESC"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FoodMatchCandidate extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_listing_id", nullable = false)
    private FoodListing foodListing;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_variant_id", nullable = false)
    private FoodVariant foodVariant;

    @Column(name = "confidence_score", precision = 5, scale = 2)
    private BigDecimal confidenceScore;

    @Column(name = "match_reason", columnDefinition = "TEXT")
    private String matchReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MatchCandidateStatus status =
            MatchCandidateStatus.NEEDS_REVIEW;

    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    @Column(name = "review_note", columnDefinition = "TEXT")
    private String reviewNote;
}