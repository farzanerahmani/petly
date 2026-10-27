package com.petly.food.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
@Entity
@Table(name = "food_nutrition")
@Getter
@Setter
@NoArgsConstructor
public class FoodNutrition extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "food_product_id",
            nullable = false,
            unique = true
    )
    private FoodProduct foodProduct;

    @Column(name = "energy_kcal_per_100g", precision = 10, scale = 2)
    private BigDecimal energyKcalPer100g;

    @Column(name = "protein_percent", precision = 5, scale = 2)
    private BigDecimal proteinPercent;

    @Column(name = "fat_percent", precision = 5, scale = 2)
    private BigDecimal fatPercent;

    @Column(name = "fiber_percent", precision = 5, scale = 2)
    private BigDecimal fiberPercent;

    @Column(name = "moisture_percent", precision = 5, scale = 2)
    private BigDecimal moisturePercent;

    @Column(length = 500)
    private String source;

    @Column(name = "verified_at")
    private Instant verifiedAt;
}

