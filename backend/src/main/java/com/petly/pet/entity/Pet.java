package com.petly.pet.entity;

import com.petly.common.entity.AuditEntity;
import com.petly.common.entity.BaseEntity;
import com.petly.pet.entity.enums.ActivityLevel;
import com.petly.pet.entity.enums.Gender;
import com.petly.pet.entity.enums.Species;
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
public class Pet extends AuditEntity {

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
}
