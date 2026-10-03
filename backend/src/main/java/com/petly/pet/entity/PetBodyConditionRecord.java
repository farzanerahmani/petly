package com.petly.pet.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
@Entity
@Table(name = "pet_body_condition_records")
@Getter
@Setter
@NoArgsConstructor
public class PetBodyConditionRecord extends BaseEntity {

    @Column(name = "score", nullable = false, updatable = false)
    @Min(1)
    @Max(9)
    private short score;

    @Column(name = "recorded_date", nullable = false, updatable = false)
    private LocalDate recordedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_id", nullable = false, updatable = false)
    private Pet pet;
}
