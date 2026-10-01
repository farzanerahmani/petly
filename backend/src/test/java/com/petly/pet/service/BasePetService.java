package com.petly.pet.service;

import com.petly.pet.controller.dto.CreatePetRequestDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetMembership;
import com.petly.pet.entity.enums.ActivityLevel;
import com.petly.pet.entity.enums.Gender;
import com.petly.pet.entity.enums.PetMembershipRole;
import com.petly.pet.entity.enums.Species;
import com.petly.pet.repository.PetMembershipRepository;
import com.petly.pet.repository.PetRepository;
import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
@ExtendWith(MockitoExtension.class)
public abstract class BasePetService {

    @Mock
    protected PetRepository petRepository;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected PetMembershipRepository petMembershipRepository;

    @InjectMocks
    protected PetService petService;

    @BeforeEach
    void setup() {
        petService = new PetService(petRepository, userRepository, petMembershipRepository);
    }

    protected User createMockedUser() {
        User user = new User();
        user.setPhoneNumber("09127808205");
        user.setId(1L);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return user;
    }

    protected Pet createMockedPet() {
        Pet pet = new Pet();
        pet.setBirthDate(LocalDate.now());
        pet.setName("Milo");
        pet.setId(1L);
        pet.setGender(Gender.FEMALE);
        pet.setBreed("breed");
        pet.setActivityLevel(ActivityLevel.HIGH);
        pet.setSpecies(Species.DOG);
        pet.setCreatedAt(Instant.now());
        pet.setUpdatedAt(Instant.now());
        return pet;
    }

    protected PetMembership createMockedPetMembership() {
        PetMembership petMembership = new PetMembership();
        petMembership.setRole(PetMembershipRole.PRIMARY_OWNER);
        petMembership.setUser(createMockedUser());
        petMembership.setPet(createMockedPet());
        return petMembership;
    }

    protected CreatePetRequestDto createMockedPetRequestDto() {
        CreatePetRequestDto requestDto = new CreatePetRequestDto();
        requestDto.setName("Milo");
        requestDto.setBreed("breed");
        requestDto.setGender(Gender.FEMALE);
        requestDto.setSpecies(Species.DOG);
        requestDto.setBirthDate(LocalDate.now());
        requestDto.setActivityLevel(ActivityLevel.HIGH);
        requestDto.setUserId(1L);
        return requestDto;
    }
}
