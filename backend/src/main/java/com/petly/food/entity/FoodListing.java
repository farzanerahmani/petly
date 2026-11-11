package com.petly.food.entity;

import com.petly.common.entity.AuditableEntity;
import com.petly.food.entity.enums.MatchingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "food_listings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_food_listings_shop_url",
                        columnNames = {"pet_shop_id", "product_url"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_food_listings_shop_active",
                        columnList = "pet_shop_id, is_active"
                ),
                @Index(
                        name = "idx_food_listings_variant",
                        columnList = "food_variant_id"
                ),
                @Index(
                        name = "idx_food_listings_matching_status",
                        columnList = "matching_status"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FoodListing extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_shop_id", nullable = false)
    private PetShop petShop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_variant_id")
    private FoodVariant foodVariant;

    @Column(name = "external_product_id", length = 200)
    private String externalProductId;

    @Column(name = "raw_product_name", nullable = false, length = 500)
    private String rawProductName;

    @Column(name = "product_url", nullable = false, columnDefinition = "TEXT")
    private String productUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "matching_status", nullable = false, length = 24)
    private MatchingStatus matchingStatus = MatchingStatus.UNMATCHED;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "first_seen_at", nullable = false)
    private Instant firstSeenAt = Instant.now();

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt = Instant.now();

    @OneToMany(mappedBy = "foodListing")
    private List<FoodListingCheck> checks = new ArrayList<>();

    @OneToMany(mappedBy = "foodListing")
    private List<FoodPrice> prices = new ArrayList<>();

    @OneToMany(mappedBy = "foodListing")
    private List<FoodMatchCandidate> matchCandidates = new ArrayList<>();
}