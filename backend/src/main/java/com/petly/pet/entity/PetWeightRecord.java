package com.petly.pet.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
@Entity
@Table(name = "pet_weight_records")
@Getter
@Setter
@NoArgsConstructor
public class PetWeightRecord extends BaseEntity {

    @Column(name = "weight_kg", nullable = false, updatable = false)
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal weightKg;

    @Column(name = "recorded_date", nullable = false, updatable = false)
    private LocalDate recordedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false, updatable = false)
    private Pet pet;
}
