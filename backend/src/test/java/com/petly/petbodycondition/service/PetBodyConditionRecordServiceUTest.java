package com.petly.petbodycondition.service;

import com.petly.pet.entity.PetBodyConditionRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
public class PetBodyConditionRecordServiceUTest extends BasePetBodyConditionRecordService {

    @Test
    void shouldCreatePetBodyConditionRecordWithValidInput() {
        var input = CreateMockedCreatePetBodyConditionRecordRequestDto();
        Mockito.when(petRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createMockedPet()));
        Mockito.when(petBodyConditionRecordRepository.save(any(PetBodyConditionRecord.class)))
                .thenReturn(createMockedPetBodyConditionRecord());
        var response = petBodyConditionRecordService.createPetBodyConditionRecord(petId, input);

        Assertions.assertEquals(petId, response.getPetId());
        Assertions.assertEquals(input.getScore(), response.getScore());
        Assertions.assertEquals(input.getRecordedDate(), response.getRecordedDate());
    }

    @Test
    void createPetWithoutUserAndThrowException() {
        Mockito.when(petRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> petBodyConditionRecordService.createPetBodyConditionRecord(petId,
                        CreateMockedCreatePetBodyConditionRecordRequestDto()));

        Mockito.verify(petRepository).findById(anyLong());
    }

    @Test
    void shouldGetPetBodyConditionRecordWithValidInput() {

        Mockito.when(petBodyConditionRecordRepository.findByPetIdOrderByRecordedDateDesc(anyLong()))
                .thenReturn(createMockedListPetBodyConditionRecord(false));
        var response =
                petBodyConditionRecordService.getPetBodyConditionRecordsByPetId(petId);

        var input = createMockedListPetBodyConditionRecord(false).get(0);
        var output = response.get(0);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(input.getPet().getId(), output.getPetId());
        Assertions.assertEquals(input.getScore(), output.getScore());
        Assertions.assertEquals(input.getRecordedDate(), output.getRecordedDate());
    }

    @Test
    void shouldGetPetBodyConditionRecordIsEmptyWithoutInput() {

        Mockito.when(petBodyConditionRecordRepository.findByPetIdOrderByRecordedDateDesc(anyLong()))
                .thenReturn(new ArrayList<>());
        var response =
                petBodyConditionRecordService.getPetBodyConditionRecordsByPetId(petId);

        Assertions.assertEquals(0, response.size());
    }

    @Test
    void shouldGetPetBodyConditionRecordCorrectOrder() {

        Mockito.when(petBodyConditionRecordRepository.findByPetIdOrderByRecordedDateDesc(anyLong()))
                .thenReturn(createMockedListPetBodyConditionRecord(true));
        var response =
                petBodyConditionRecordService.getPetBodyConditionRecordsByPetId(petId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals((short) 1, response.get(0).getScore());
        Assertions.assertEquals((short) 2, response.get(1).getScore());
    }
}
