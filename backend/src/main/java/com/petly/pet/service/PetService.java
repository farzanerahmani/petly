package com.petly.pet.service;

import com.petly.pet.controller.dto.CreatePetRequestDto;
import com.petly.pet.controller.dto.CreatePetResponseDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 9/27/2026
 */
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public CreatePetResponseDto createPet(CreatePetRequestDto requestDto) {
        Pet pet = new Pet();
        pet.setName(requestDto.getName());
        pet.setSpecies(requestDto.getSpecies());

        Pet savePet = petRepository.save(pet);

        CreatePetResponseDto responseDto = new CreatePetResponseDto();
        responseDto.setId(savePet.getId());
        responseDto.setName(savePet.getName());
        responseDto.setSpecies(savePet.getSpecies());
        return responseDto;
    }
}
