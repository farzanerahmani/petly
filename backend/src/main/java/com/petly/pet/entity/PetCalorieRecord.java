package com.petly.pet.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author farzane.rahmani
 * @created 10/17/2026
 */
@Entity
@Table(name = "pet_calorie_records")
@Getter
@Setter
@NoArgsConstructor
public class PetCalorieRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "weight_kg", nullable = false, precision = 7, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "age_months", nullable = false)
    private Integer ageMonths;

    @Column(name = "activity_level", nullable = false)
    private String activityLevel;

    @Column(name = "neutered", nullable = false)
    private Boolean neutered;

    @Column(name = "rer_kcal", nullable = false, precision = 10, scale = 2)
    private BigDecimal rerKcal;

    @Column(name = "factor", nullable = false, precision = 5, scale = 2)
    private BigDecimal factor;

    @Column(name = "mer_kcal", nullable = false, precision = 10, scale = 2)
    private BigDecimal merKcal;
}
