package com.petly.pet.service;

import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetCalorieRecord;
import com.petly.pet.repository.PetCalorieRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author farzane.rahmani
 * @created 10/18/2026
 */
@Service
@RequiredArgsConstructor
public class PetCalorieRecordService {

    private final PetCalorieRecordRepository repository;


    public PetCalorieRecord saveRecord(
            Pet pet,
            BigDecimal weightKg,
            Integer ageMonths,
            String activityLevel,
            Boolean neutered,
            CalorieEstimate estimate
    ) {
        PetCalorieRecord record = new PetCalorieRecord();
        record.setPet(pet);
        record.setWeightKg(weightKg);
        record.setAgeMonths(ageMonths);
        record.setActivityLevel(activityLevel);
        record.setNeutered(neutered);
        record.setRerKcal(estimate.getRerKcal());
        record.setFactor(estimate.getFactor());
        record.setMerKcal(estimate.getMerKcal());

        return repository.save(record);
    }
}

