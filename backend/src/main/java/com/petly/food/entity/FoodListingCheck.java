package com.petly.food.entity;

import com.petly.common.entity.BaseEntity;
import com.petly.food.entity.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "food_listing_checks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_food_listing_checks_id_listing",
                        columnNames = {"id", "food_listing_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_food_listing_checks_listing_checked",
                        columnList = "food_listing_id, checked_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FoodListingCheck extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_listing_id", nullable = false)
    private FoodListing foodListing;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false, length = 20)
    private AvailabilityStatus availability = AvailabilityStatus.UNKNOWN;

    @Column(name = "stock_text", length = 500)
    private String stockText;

    @Column(name = "scrape_error", columnDefinition = "TEXT")
    private String scrapeError;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt = Instant.now();
}