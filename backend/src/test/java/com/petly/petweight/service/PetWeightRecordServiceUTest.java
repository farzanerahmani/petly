package com.petly.petweight.service;

import com.petly.pet.entity.PetWeightRecord;
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
public class PetWeightRecordServiceUTest extends BasePetWeightRecordService {

    @Test
    void shouldCreatePetWeightRecordWithValidInput() {
        var input = CreateMockedPetWeightRecordRequestDto();
        Mockito.when(petRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createMockedPet()));
        Mockito.when(petWeightRecordRepository.save(any(PetWeightRecord.class))).thenReturn(createMockedPetWeightRecord());
        var response = petWeightRecordService.createPetWeight(petId, input);

        Assertions.assertEquals(petId, response.getPetId());
        Assertions.assertEquals(input.getWeightKg(), response.getWeightKg());
        Assertions.assertEquals(input.getRecordedDate(), response.getRecordedDate());
    }

    @Test
    void createPetWithoutUserAndThrowException() {
        Mockito.when(petRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> petWeightRecordService.createPetWeight(petId, CreateMockedPetWeightRecordRequestDto()));

        Mockito.verify(petRepository).findById(anyLong());
    }

    @Test
    void shouldGetPetWeightRecordWithValidInput() {

        Mockito.when(petWeightRecordRepository.findByPetIdOrderByRecordedDateDesc(anyLong()))
                .thenReturn(createMockedListPetWeightRecord());
        var response =
                petWeightRecordService.getAllPetWeightRecordsByPetId(petId);

        var input = createMockedListPetWeightRecord().get(0);
        var output = response.get(0);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(input.getPet().getId(), output.getPetId());
        Assertions.assertEquals(input.getWeightKg(), output.getWeightKg());
        Assertions.assertEquals(input.getRecordedDate(), output.getRecordedDate());
    }

    @Test
    void shouldGetPetWeightRecordIsEmptyWithoutInput() {

        Mockito.when(petWeightRecordRepository.findByPetIdOrderByRecordedDateDesc(anyLong()))
                .thenReturn(new ArrayList<>());
        var response =
                petWeightRecordService.getAllPetWeightRecordsByPetId(petId);

        Assertions.assertEquals(0, response.size());
    }
}
