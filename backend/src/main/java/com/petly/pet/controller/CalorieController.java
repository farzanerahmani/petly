package com.petly.pet.controller;

import com.petly.pet.controller.dto.CalorieCalculationRequest;
import com.petly.pet.service.CalorieCalculator;
import com.petly.pet.service.CalorieEstimate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author farzane.rahmani
 * @created 10/17/2026
 */


@RestController
@RequestMapping("/api/v1/calories")
@RequiredArgsConstructor
public class CalorieController {
    private final CalorieCalculator calorieCalculator;

    @PostMapping("/calculate")
    public CalorieEstimate calculate(
            @RequestBody CalorieCalculationRequest request
    ) {
        int ageMonths = calculateAgeMonths(request.getBirthDate());

        return calorieCalculator.calculate(
                request.getPetId(),
                request.getSpecies(),
                request.getWeightKg(),
                ageMonths,
                request.isNeutered(),
                request.getActivityLevel()
        );
    }

    private int calculateAgeMonths(java.time.LocalDate birthDate) {
        if (birthDate == null || birthDate.isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Invalid birth date");
        }

        return (int) java.time.temporal.ChronoUnit.MONTHS.between(
                birthDate,
                java.time.LocalDate.now()
        );
    }
}
