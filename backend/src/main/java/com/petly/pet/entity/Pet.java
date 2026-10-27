package com.petly.pet.entity;

import com.petly.common.entity.AuditableEntity;
import com.petly.pet.entity.enums.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 9/29/2026
 */
@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
public class Pet extends AuditableEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Species species;

    @Column(length = 100)
    private String breed;
    //todo: convert to reference data/ breed entity , if i choose dog then system show all of dog's breed

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", length = 30)
    private ActivityLevel activityLevel;

    @Column(name = "neutered")
    private Boolean neutered;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type_preference", length = 30)
    private FoodTypePreference foodTypePreference = FoodTypePreference.NO_PREFERENCE;

    @Column(name = "current_food", length = 200)
    private String currentFood;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_food_satisfaction", length = 20)
    private CurrentFoodSatisfaction currentFoodSatisfaction = CurrentFoodSatisfaction.UNKNOWN;
}
