package com.petly.pet.controller;

import com.petly.common.response.ApiResponse;
import com.petly.pet.controller.dto.*;
import com.petly.pet.entity.Pet;
import com.petly.pet.service.PetBodyConditionRecordService;
import com.petly.pet.service.PetService;
import com.petly.pet.service.PetWeightRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 9/29/2026
 */
@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    private final PetBodyConditionRecordService petBodyConditionRecordService;
    private final PetWeightRecordService petWeightRecordService;

    @GetMapping
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreatePetResponseDto>> createPet(@Valid @RequestBody CreatePetRequestDto requestDto) {
        CreatePetResponseDto responseDto =
                petService.createPet(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }

    @GetMapping("/{petId}/getAllPetWeightRecord")
    public List<PetWeightRecordResponseDto> getAllPetWeightRecord(@PathVariable Long petId) {

        return petWeightRecordService.getAllPetWeightRecordsByPetId(petId);
    }

    @PostMapping("/{petId}/createPetWeightRecord")
    public ResponseEntity<ApiResponse<PetWeightRecordResponseDto>> createPetWeightRecord(
            @PathVariable Long petId,
            @Valid @RequestBody CreatePetWeightRecordRequestDto requestDto) {
        PetWeightRecordResponseDto responseDto =
                petWeightRecordService.createPetWeight(petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }

    @GetMapping("/{petId}/getAllPetBodyConditionRecord")
    public List<PetBodyConditionRecordResponseDto> getAllPetBodyConditionRecord(@PathVariable Long petId) {

        return petBodyConditionRecordService.getPetBodyConditionRecordsByPetId(petId);
    }

    @PostMapping("/{petId}/createPetBodyConditionRecord")
    public ResponseEntity<ApiResponse<PetBodyConditionRecordResponseDto>> createPetBodyConditionRecord(
            @PathVariable Long petId,
            @Valid @RequestBody CreatePetBodyConditionRecordRequestDto requestDto) {
        PetBodyConditionRecordResponseDto responseDto =
                petBodyConditionRecordService.createPetBodyConditionRecord(petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }
}
