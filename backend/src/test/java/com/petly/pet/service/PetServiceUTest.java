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
}
