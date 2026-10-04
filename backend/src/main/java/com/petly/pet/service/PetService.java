package com.petly.pet.service;

import com.petly.pet.controller.dto.CreatePetRequestDto;
import com.petly.pet.controller.dto.PetResponseDto;
import com.petly.pet.controller.dto.UpdatePetRequestDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetMembership;
import com.petly.pet.entity.enums.PetMembershipRole;
import com.petly.pet.repository.PetMembershipRepository;
import com.petly.pet.repository.PetRepository;
import com.petly.pet.service.conventor.PetConverter;
import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 9/27/2026
 */
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMembershipRepository petMembershipRepository;

    private final PetConverter petConverter;

    public PetResponseDto getPetById(Long petId) {

        var pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("pet not found"));
        return petConverter.convertToPetResponseDto(pet);
    }

    @Transactional
    public PetResponseDto createPet(CreatePetRequestDto requestDto) {

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        Pet pet = new Pet();
        pet.setName(requestDto.getName());
        pet.setSpecies(requestDto.getSpecies());
        pet.setGender(requestDto.getGender());
        pet.setBreed(requestDto.getBreed());
        pet.setActivityLevel(requestDto.getActivityLevel());
        pet.setBirthDate(requestDto.getBirthDate());

        Pet savedPet = petRepository.save(pet);
        PetMembership membership = new PetMembership();
        membership.setUser(user);
        membership.setPet(savedPet);
        membership.setRole(PetMembershipRole.PRIMARY_OWNER);
        petMembershipRepository.save(membership);

        return petConverter.convertToPetResponseDto(savedPet);
    }

    @Transactional
    public void updatePet(Long petId, UpdatePetRequestDto requestDto) {

        var pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("pet not found"));

        pet.setGender(requestDto.getGender());
        pet.setName(requestDto.getName());
        pet.setActivityLevel(requestDto.getActivityLevel());
        pet.setBreed(requestDto.getBreed());
        pet.setBirthDate(requestDto.getBirthDate());
        petRepository.save(pet);
    }

    public List<PetResponseDto> getAllPets() {
        var pets = petRepository.findAll();
        return petConverter.convertToListPetResponseDto(pets);
    }
}
