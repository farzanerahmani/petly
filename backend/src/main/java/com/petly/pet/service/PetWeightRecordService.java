package com.petly.pet.service;

import com.petly.pet.controller.dto.CreatePetWeightRecordRequestDto;
import com.petly.pet.controller.dto.PetWeightRecordResponseDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.entity.PetWeightRecord;
import com.petly.pet.repository.PetRepository;
import com.petly.pet.repository.PetWeightRecordRepository;
import lombok.RequiredArgsConstructor;
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
public class PetWeightRecordService {
    private final PetWeightRecordRepository petWeightRecordRepository;
    private final PetRepository petRepository;

    public List<PetWeightRecordResponseDto> getAllPetWeightRecordsByPetId(Long petId) {
        var petWeight = petWeightRecordRepository.findByPetIdOrderByRecordedDateDesc(
                petId);
        List<PetWeightRecordResponseDto> responseDtos = new ArrayList<>();

        for (var item : petWeight) {
            PetWeightRecordResponseDto petWeightRecordResponseDto = new PetWeightRecordResponseDto();
            petWeightRecordResponseDto.setPetId(item.getPet().getId());
            petWeightRecordResponseDto.setId(item.getId());
            petWeightRecordResponseDto.setRecordedDate(item.getRecordedDate());
            petWeightRecordResponseDto.setWeightKg(item.getWeightKg());
            petWeightRecordResponseDto.setCreatedAt(item.getCreatedAt());
            responseDtos.add(petWeightRecordResponseDto);
        }
        return responseDtos;
    }

    @Transactional
    public PetWeightRecordResponseDto createPetWeight(Long petId, CreatePetWeightRecordRequestDto requestDto) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("pet not found"));

        PetWeightRecord petWeightRecord = new PetWeightRecord();
        petWeightRecord.setPet(pet);
        petWeightRecord.setWeightKg(requestDto.getWeightKg());
        petWeightRecord.setRecordedDate(requestDto.getRecordedDate());

        PetWeightRecord savedPetWeight = petWeightRecordRepository.save(petWeightRecord);

        PetWeightRecordResponseDto responseDto = new PetWeightRecordResponseDto();
        responseDto.setId(savedPetWeight.getId());
        responseDto.setWeightKg(savedPetWeight.getWeightKg());
        responseDto.setRecordedDate(savedPetWeight.getRecordedDate());
        responseDto.setCreatedAt(savedPetWeight.getCreatedAt());
        responseDto.setPetId(savedPetWeight.getPet().getId());
        return responseDto;
    }
}
