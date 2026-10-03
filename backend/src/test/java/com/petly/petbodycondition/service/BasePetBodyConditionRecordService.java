package com.petly.petbodycondition.service;

import com.petly.pet.controller.dto.CreatePetBodyConditionRecordRequestDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetBodyConditionRecord;
import com.petly.pet.entity.enums.ActivityLevel;
import com.petly.pet.entity.enums.Gender;
import com.petly.pet.entity.enums.Species;
import com.petly.pet.repository.PetBodyConditionRecordRepository;
import com.petly.pet.repository.PetRepository;
import com.petly.pet.service.PetBodyConditionRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
@ExtendWith(MockitoExtension.class)
public abstract class BasePetBodyConditionRecordService {
    protected final Long petId = 1L;

    @Mock
    protected PetRepository petRepository;

    @Mock
    protected PetBodyConditionRecordRepository petBodyConditionRecordRepository;

    @InjectMocks
    protected PetBodyConditionRecordService petBodyConditionRecordService;

    @BeforeEach
    void setup() {
        petBodyConditionRecordService = new PetBodyConditionRecordService(petRepository, petBodyConditionRecordRepository);
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

    protected CreatePetBodyConditionRecordRequestDto CreateMockedCreatePetBodyConditionRecordRequestDto() {
        CreatePetBodyConditionRecordRequestDto requestDto = new CreatePetBodyConditionRecordRequestDto();
        requestDto.setRecordedDate(LocalDate.now());
        requestDto.setScore((short) 2);
        return requestDto;
    }

    protected PetBodyConditionRecord createMockedPetBodyConditionRecord() {
        PetBodyConditionRecord petBodyConditionRecord = new PetBodyConditionRecord();
        petBodyConditionRecord.setPet(createMockedPet());
        petBodyConditionRecord.setRecordedDate(LocalDate.now());
        petBodyConditionRecord.setScore((short) 2);
        petBodyConditionRecord.setCreatedAt(Instant.now());
        return petBodyConditionRecord;
    }

    protected List<PetBodyConditionRecord> createMockedListPetBodyConditionRecord(boolean isTwoRecord) {
        List<PetBodyConditionRecord> list = new ArrayList<>();
        if (!isTwoRecord) {
            list.add(createMockedPetBodyConditionRecord());
            return list;
        } else {
            for (int i = 1; i <= 2; i++) {
                PetBodyConditionRecord petBodyConditionRecord = new PetBodyConditionRecord();

                petBodyConditionRecord.setPet(createMockedPet());
                petBodyConditionRecord.setRecordedDate(LocalDate.now());
                petBodyConditionRecord.setScore((short) i);
                petBodyConditionRecord.setCreatedAt(Instant.now());
                list.add(petBodyConditionRecord);
            }

            return list;
        }

    }

}
