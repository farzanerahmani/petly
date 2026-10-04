package com.petly.pet.service;

import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetMembership;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
public class PetServiceUTest extends BasePetService {

    @Test
    void shouldCreatePetWithValidInput() {
        var input = createMockedPetRequestDto();
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createMockedUser()));
        Mockito.when(petRepository.save(any(Pet.class))).thenReturn(createMockedPet());
        Mockito.when(petMembershipRepository.save(any(PetMembership.class))).thenReturn(createMockedPetMembership());
        Mockito.when(petConverter.convertToPetResponseDto(any())).thenReturn(createMockedPetResponseDto());

        var response = petService.createPet(input);

        Assertions.assertEquals(response.getName(), input.getName());
        Assertions.assertEquals(response.getBreed(), input.getBreed());
        Assertions.assertEquals(response.getGender(), input.getGender());
        Assertions.assertEquals(response.getSpecies(), input.getSpecies());
        Assertions.assertEquals(response.getActivityLevel(), input.getActivityLevel());
        Assertions.assertEquals(response.getBirthDate(), input.getBirthDate());
    }

    @Test
    void createPetWithoutUserAndThrowException() {
        Mockito.when(userRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> petService.createPet(createMockedPetRequestDto()));

        Mockito.verify(userRepository).findById(anyLong());
        Mockito.verify(petRepository, Mockito.never()).save(any(Pet.class));
        Mockito.verify(petMembershipRepository, Mockito.never()).save(any(PetMembership.class));
    }

    @Test
    void getPetThrowException() {
        Mockito.when(petRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> petService.getPetById(petId));

        Mockito.verify(petRepository).findById(anyLong());
    }

    @Test
    void shouldGetPetByIdWithValidInput() {

        Mockito.when(petRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(createMockedPet()));
        Mockito.when(petConverter.convertToPetResponseDto(any()))
                .thenReturn(createMockedPetResponseDto());
        var response =
                petService.getPetById(petId);

        var input = createMockedPet();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(input.getName(), response.getName());
        Assertions.assertEquals(input.getGender(), response.getGender());
        Assertions.assertEquals(input.getBreed(), response.getBreed());
        Assertions.assertEquals(input.getBirthDate(), response.getBirthDate());
        Assertions.assertEquals(input.getSpecies(), response.getSpecies());
        Assertions.assertEquals(input.getActivityLevel(), response.getActivityLevel());
    }

    @Test
    void shouldGetAllPetsWithValidInput() {

        Mockito.when(petRepository.findAll())
                .thenReturn(createMockedListPet());
        Mockito.when(petConverter.convertToListPetResponseDto(any()))
                .thenReturn(createMockedListPetResponseDto());
        var response =
                petService.getAllPets();

        var input = createMockedPet();
        var output = response.get(0);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(input.getName(), output.getName());
        Assertions.assertEquals(input.getGender(), output.getGender());
        Assertions.assertEquals(input.getBreed(), output.getBreed());
        Assertions.assertEquals(input.getBirthDate(), output.getBirthDate());
        Assertions.assertEquals(input.getSpecies(), output.getSpecies());
        Assertions.assertEquals(input.getActivityLevel(), output.getActivityLevel());
    }

    @Test
    void updatePetThrowException() {
        Mockito.when(petRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> petService.updatePet(petId, any()));

        Mockito.verify(petRepository).findById(anyLong());
    }

    @Test
    void shouldUpdatePetByIdWithValidInput() {

        Mockito.when(petRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(createMockedPet()));

        petService.updatePet(petId, createMockedUpdatePetRequestDto());

        Mockito.verify(petRepository,Mockito.times(1)).findById(anyLong());

    }
}
