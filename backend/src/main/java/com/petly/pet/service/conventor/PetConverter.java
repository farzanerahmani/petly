package com.petly.pet.service.conventor;

import com.petly.pet.controller.dto.PetResponseDto;
import com.petly.pet.entity.Pet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author farzane.rahmani
 * @created 10/3/2026
 */
public class PetConverter {

    public PetResponseDto convertToPetResponseDto(Pet pet){
        PetResponseDto responseDto = new PetResponseDto();
        responseDto.setId(pet.getId());
        responseDto.setName(pet.getName());
        responseDto.setSpecies(pet.getSpecies());
        responseDto.setBreed(pet.getBreed());
        responseDto.setGender(pet.getGender());
        responseDto.setActivityLevel(pet.getActivityLevel());
        responseDto.setBirthDate(pet.getBirthDate());
        responseDto.setCreatedAt(pet.getCreatedAt());
        responseDto.setUpdatedAt(pet.getUpdatedAt());
        return responseDto;
    }

    public List<PetResponseDto> convertToListPetResponseDto(List<Pet> pets){
        return pets.stream().map(this::convertToPetResponseDto).collect(Collectors.toList());
    }
}
