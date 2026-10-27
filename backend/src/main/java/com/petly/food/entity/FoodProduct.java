package com.petly.food.entity;

import com.petly.common.entity.AuditableEntity;
import com.petly.food.entity.enums.*;
import com.petly.pet.entity.enums.Species;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
@Entity
@Table(
        name = "food_products",
        indexes = {
                @Index(
                        name = "idx_food_products_species_type_active",
                        columnList = "species, food_type, is_active"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FoodProduct extends AuditableEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 150)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Species species;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type", nullable = false, length = 20)
    private FoodType foodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "life_stage", length = 30)
    private FoodLifeStage lifeStage;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @OneToOne(
            mappedBy = "foodProduct",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private FoodNutrition nutrition;

    @OneToMany(
            mappedBy = "foodProduct",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FoodPrice> prices = new ArrayList<>();

    public void setNutrition(FoodNutrition nutrition) {
        this.nutrition = nutrition;

        if (nutrition != null) {
            nutrition.setFoodProduct(this);
        }
    }

    public void addPrice(FoodPrice price) {
        prices.add(price);
        price.setFoodProduct(this);
    }

    public void removePrice(FoodPrice price) {
        prices.remove(price);
        price.setFoodProduct(null);
    }
}

