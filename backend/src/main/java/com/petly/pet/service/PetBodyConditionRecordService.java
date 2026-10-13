package com.petly.pet.service;

import com.petly.auth.service.CurrentUserService;
import com.petly.pet.controller.dto.CreatePetBodyConditionRecordRequestDto;
import com.petly.pet.controller.dto.PetBodyConditionRecordResponseDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetBodyConditionRecord;
import com.petly.pet.repository.PetBodyConditionRecordRepository;
import com.petly.pet.repository.PetMembershipRepository;
import com.petly.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/2/2026
 */
@Service
@RequiredArgsConstructor
public class PetBodyConditionRecordService {
    private final PetRepository petRepository;
    private final PetBodyConditionRecordRepository petBodyConditionRecordRepository;

    private final PetMembershipRepository petMembershipRepository;

    private final CurrentUserService currentUserService;

    public List<PetBodyConditionRecordResponseDto> getPetBodyConditionRecordsByPetId(
            Long petId) {

        ensurePetBelongsToCurrentUser(petId);

        var bodyCondition =
                petBodyConditionRecordRepository.findByPetIdOrderByRecordedDateDesc(petId);
        List<PetBodyConditionRecordResponseDto> responseDtos = new ArrayList<>();
        for (var item : bodyCondition) {
            PetBodyConditionRecordResponseDto petBodyConditionRecordResponseDto = new PetBodyConditionRecordResponseDto();

            petBodyConditionRecordResponseDto.setPetId(item.getPet().getId());
            petBodyConditionRecordResponseDto.setId(item.getId());
            petBodyConditionRecordResponseDto.setRecordedDate(item.getRecordedDate());
            petBodyConditionRecordResponseDto.setScore(item.getScore());
            petBodyConditionRecordResponseDto.setCreatedAt(item.getCreatedAt());
            responseDtos.add(petBodyConditionRecordResponseDto);
        }
        return responseDtos;
    }

    @Transactional
    public PetBodyConditionRecordResponseDto createPetBodyConditionRecord(Long petId, CreatePetBodyConditionRecordRequestDto requestDto) {
        ensurePetBelongsToCurrentUser(petId);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("pet not found"));

        PetBodyConditionRecord petBodyConditionRecord = new PetBodyConditionRecord();
        petBodyConditionRecord.setScore(requestDto.getScore());
        petBodyConditionRecord.setPet(pet);
        petBodyConditionRecord.setRecordedDate(requestDto.getRecordedDate());

        PetBodyConditionRecord savedPetBodyConditionRecord =
                petBodyConditionRecordRepository.save(petBodyConditionRecord);

        PetBodyConditionRecordResponseDto responseDto = new PetBodyConditionRecordResponseDto();
        responseDto.setId(savedPetBodyConditionRecord.getId());
        responseDto.setScore(savedPetBodyConditionRecord.getScore());
        responseDto.setRecordedDate(savedPetBodyConditionRecord.getRecordedDate());
        responseDto.setCreatedAt(savedPetBodyConditionRecord.getCreatedAt());
        responseDto.setPetId(savedPetBodyConditionRecord.getPet().getId());
        return responseDto;
    }

    private void ensurePetBelongsToCurrentUser(Long petId) {
        Long userId = currentUserService.getCurrentUserId();

        if (!petMembershipRepository.existsByUserIdAndPetId(userId, petId)) {
            throw new AccessDeniedException(
                    "You do not have access to this pet"
            );
        }
    }
}
