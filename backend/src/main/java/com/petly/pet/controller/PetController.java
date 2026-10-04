package com.petly.pet.controller;

import com.petly.common.response.ApiResponse;
import com.petly.pet.controller.dto.CreatePetBodyConditionRecordRequestDto;
import com.petly.pet.controller.dto.CreatePetRequestDto;
import com.petly.pet.controller.dto.CreatePetWeightRecordRequestDto;
import com.petly.pet.controller.dto.PetBodyConditionRecordResponseDto;
import com.petly.pet.controller.dto.PetResponseDto;
import com.petly.pet.controller.dto.PetWeightRecordResponseDto;
import com.petly.pet.controller.dto.UpdatePetRequestDto;
import com.petly.pet.service.PetBodyConditionRecordService;
import com.petly.pet.service.PetService;
import com.petly.pet.service.PetWeightRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetBodyConditionRecordService petBodyConditionRecordService;
    private final PetWeightRecordService petWeightRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<PetResponseDto>> createPet(
            @Valid @RequestBody CreatePetRequestDto requestDto) {

        PetResponseDto responseDto = petService.createPet(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<ApiResponse<PetResponseDto>> getPetById(
            @PathVariable Long petId) {

        PetResponseDto responseDto = petService.getPetById(petId);

        return ResponseEntity.ok(
                ApiResponse.success(responseDto)
        );
    }

    @PutMapping("/{petId}")
    public ResponseEntity<ApiResponse<Long>> updatePet(
            @PathVariable Long petId,
            @Valid @RequestBody UpdatePetRequestDto requestDto) {

        petService.updatePet(petId, requestDto);

        return ResponseEntity.ok(
                ApiResponse.success(petId)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PetResponseDto>>> getAllPets() {

        List<PetResponseDto> responseDto = petService.getAllPets();

        return ResponseEntity.ok(
                ApiResponse.success(responseDto)
        );
    }

    @PostMapping("/{petId}/weight-records")
    public ResponseEntity<ApiResponse<PetWeightRecordResponseDto>> createPetWeightRecord(
            @PathVariable Long petId,
            @Valid @RequestBody CreatePetWeightRecordRequestDto requestDto) {

        PetWeightRecordResponseDto responseDto =
                petWeightRecordService.createPetWeight(petId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }

    @GetMapping("/{petId}/weight-records")
    public ResponseEntity<ApiResponse<List<PetWeightRecordResponseDto>>> getAllPetWeightRecords(
            @PathVariable Long petId) {

        List<PetWeightRecordResponseDto> responseDto =
                petWeightRecordService.getAllPetWeightRecordsByPetId(petId);

        return ResponseEntity.ok(
                ApiResponse.success(responseDto)
        );
    }

    @PostMapping("/{petId}/body-condition-records")
    public ResponseEntity<ApiResponse<PetBodyConditionRecordResponseDto>> createPetBodyConditionRecord(
            @PathVariable Long petId,
            @Valid @RequestBody CreatePetBodyConditionRecordRequestDto requestDto) {

        PetBodyConditionRecordResponseDto responseDto =
                petBodyConditionRecordService.createPetBodyConditionRecord(
                        petId,
                        requestDto
                );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }

    @GetMapping("/{petId}/body-condition-records")
    public ResponseEntity<ApiResponse<List<PetBodyConditionRecordResponseDto>>> getAllPetBodyConditionRecords(
            @PathVariable Long petId) {

        List<PetBodyConditionRecordResponseDto> responseDto =
                petBodyConditionRecordService.getPetBodyConditionRecordsByPetId(petId);

        return ResponseEntity.ok(
                ApiResponse.success(responseDto)
        );
    }
}