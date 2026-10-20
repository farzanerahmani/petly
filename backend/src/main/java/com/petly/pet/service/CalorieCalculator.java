package com.petly.pet.service;

import com.petly.pet.entity.Pet;
import com.petly.pet.repository.PetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author farzane.rahmani
 * @created 10/17/2026
 */
@Service
@AllArgsConstructor
public class CalorieCalculator {
    private final PetCalorieRecordService petCalorieRecordService;
    private final PetRepository petRepository;

    public CalorieEstimate calculate(
            Long petId,
            String species,
            BigDecimal weightKg,
            int ageMonths,
            boolean neutered,
            String activityLevel
    ) {
        validate(species, weightKg, ageMonths, activityLevel);

        double weight = weightKg.doubleValue();

        // Resting Energy Requirement
        double rer = 70.0 * Math.pow(weight, 0.75);

        double factor = getFactor(
                species,
                ageMonths,
                neutered,
                activityLevel
        );

        // Maintenance Energy Requirement
        double mer = rer * factor;

        var caloriesEstimate = new CalorieEstimate(
                BigDecimal.valueOf(rer)
                        .setScale(0, RoundingMode.HALF_UP),
                BigDecimal.valueOf(factor)
                        .setScale(2, RoundingMode.HALF_UP),
                BigDecimal.valueOf(mer)
                        .setScale(0, RoundingMode.HALF_UP)
        );

        Pet pet = petRepository.getReferenceById(petId);
        petCalorieRecordService.saveRecord(pet, weightKg, ageMonths, activityLevel, neutered, caloriesEstimate);
        return caloriesEstimate;
    }

    private double getFactor(
            String species,
            int ageMonths,
            boolean neutered,
            String activityLevel
    ) {
        // General growth estimates
        if (ageMonths < 12) {
            if ("DOG".equals(species)) {
                return ageMonths < 4 ? 3.0 : 2.0;
            }

            return 2.5; // CAT
        }

        double min;
        double max;

        if ("DOG".equals(species)) {
            min = neutered ? 1.4 : 1.6;
            max = neutered ? 1.6 : 1.8;
        } else {
            min = neutered ? 1.2 : 1.4;
            max = neutered ? 1.4 : 1.6;
        }

        switch (activityLevel) {
            case "LOW":
                return min;

            case "MODERATE":
                return (min + max) / 2.0;

            case "HIGH":
            case "VERY_HIGH":
                return max;

            default:
                throw new IllegalArgumentException(
                        "Unsupported activity level"
                );
        }
    }

    private void validate(
            String species,
            BigDecimal weightKg,
            int ageMonths,
            String activityLevel
    ) {
        if (!"DOG".equals(species) && !"CAT".equals(species)) {
            throw new IllegalArgumentException(
                    "Species must be DOG or CAT"
            );
        }

        if (weightKg == null
                || weightKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Weight must be greater than zero"
            );
        }

        if (ageMonths < 0) {
            throw new IllegalArgumentException(
                    "Age cannot be negative"
            );
        }

        if (!"LOW".equals(activityLevel)
                && !"MODERATE".equals(activityLevel)
                && !"HIGH".equals(activityLevel)
                && !"VERY_HIGH".equals(activityLevel)) {
            throw new IllegalArgumentException(
                    "Unsupported activity level"
            );
        }
    }
}


