package com.petly.petweight.service;

import com.petly.pet.controller.dto.CreatePetWeightRecordRequestDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetWeightRecord;
import com.petly.pet.entity.enums.ActivityLevel;
import com.petly.pet.entity.enums.Gender;
import com.petly.pet.entity.enums.Species;
import com.petly.pet.repository.PetRepository;
import com.petly.pet.repository.PetWeightRecordRepository;
import com.petly.pet.service.PetWeightRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
@ExtendWith(MockitoExtension.class)
public abstract class BasePetWeightRecordService {
    protected final Long petId = 1L;

    @Mock
    protected PetRepository petRepository;

    @Mock
    protected PetWeightRecordRepository petWeightRecordRepository;

    @InjectMocks
    protected PetWeightRecordService petWeightRecordService;

    @BeforeEach
    void setup() {
        petWeightRecordService = new PetWeightRecordService(petWeightRecordRepository, petRepository);
    }

    protected Pet createMockedPet() {
        Pet pet = new Pet();
        pet.setBirthDate(LocalDate.now());
        pet.setName("Milo");
        pet.setId(petId);
        pet.setGender(Gender.FEMALE);
        pet.setBreed("breed");
        pet.setActivityLevel(ActivityLevel.HIGH);
        pet.setSpecies(Species.DOG);
        pet.setCreatedAt(Instant.now());
        pet.setUpdatedAt(Instant.now());
        return pet;
    }

    protected CreatePetWeightRecordRequestDto CreateMockedPetWeightRecordRequestDto() {
        CreatePetWeightRecordRequestDto requestDto = new CreatePetWeightRecordRequestDto();
        requestDto.setRecordedDate(LocalDate.now());
        requestDto.setWeightKg(BigDecimal.TEN);
        return requestDto;
    }

    protected PetWeightRecord createMockedPetWeightRecord() {
        PetWeightRecord petWeightRecord = new PetWeightRecord();
        petWeightRecord.setPet(createMockedPet());
        petWeightRecord.setRecordedDate(LocalDate.now());
        petWeightRecord.setWeightKg(BigDecimal.TEN);
        petWeightRecord.setCreatedAt(Instant.now());
        return petWeightRecord;
    }

    protected List<PetWeightRecord> createMockedListPetWeightRecord() {
        List<PetWeightRecord> list = new ArrayList<>();
        list.add(createMockedPetWeightRecord());
        return list;
    }
}
