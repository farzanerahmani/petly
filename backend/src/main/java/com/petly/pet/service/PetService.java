package com.petly.pet.service;

import com.petly.pet.controller.dto.CreatePetRequestDto;
import com.petly.pet.controller.dto.CreatePetResponseDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetMembership;
import com.petly.pet.entity.enums.PetMembershipRole;
import com.petly.pet.repository.PetMembershipRepository;
import com.petly.pet.repository.PetRepository;
import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Transactional
    public CreatePetResponseDto createPet(CreatePetRequestDto requestDto) {

        User user  = userRepository.findById(requestDto.getUserId())
                .orElseThrow(()->new IllegalArgumentException("user not found"));

        Pet pet = new Pet();
        pet.setName(requestDto.getName());
        pet.setSpecies(requestDto.getSpecies());
        pet.setGender(requestDto.getGender());
        pet.setBreed(requestDto.getBreed());
        pet.setActivityLevel(requestDto.getActivityLevel());
        pet.setBirthDate(requestDto.getBirthDate());

        Pet savedPet = petRepository.save(pet);
        PetMembership membership =new PetMembership();
        membership.setUser(user);
        membership.setPet(savedPet);
        membership.setRole(PetMembershipRole.PRIMARY_OWNER);
        petMembershipRepository.save(membership);

        CreatePetResponseDto responseDto = new CreatePetResponseDto();
        responseDto.setId(savedPet.getId());
        responseDto.setName(savedPet.getName());
        responseDto.setSpecies(savedPet.getSpecies());
        responseDto.setBreed(savedPet.getBreed());
        responseDto.setGender(savedPet.getGender());
        responseDto.setActivityLevel(savedPet.getActivityLevel());
        responseDto.setBirthDate(savedPet.getBirthDate());
        responseDto.setCreatedAt(savedPet.getCreatedAt());
        responseDto.setUpdatedAt(savedPet.getUpdatedAt());
        return responseDto;
    }
}
