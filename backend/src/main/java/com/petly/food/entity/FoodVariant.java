package com.petly.food.entity;

import com.petly.common.entity.AuditableEntity;
import com.petly.food.entity.enums.WeightStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "food_variants",
        indexes = {
                @Index(
                        name = "idx_food_variants_product",
                        columnList = "food_product_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FoodVariant extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_product_id", nullable = false)
    private FoodProduct foodProduct;

    @Column(name = "package_weight_grams")
    private Integer packageWeightGrams;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_status", nullable = false, length = 20)
    private WeightStatus weightStatus;

    @OneToMany(mappedBy = "foodVariant")
    private List<FoodListing> listings = new ArrayList<>();

    @OneToMany(mappedBy = "foodVariant")
    private List<FoodMatchCandidate> matchCandidates = new ArrayList<>();
}