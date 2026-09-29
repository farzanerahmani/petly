package com.petly.pet.controller;

import com.petly.common.response.ApiResponse;
import com.petly.pet.controller.dto.CreatePetRequestDto;
import com.petly.pet.controller.dto.CreatePetResponseDto;
import com.petly.pet.entity.Pet;
import com.petly.pet.service.PetService;
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

    @GetMapping
    public List<Pet> getAllPets(){
        return petService.getAllPets();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CreatePetResponseDto>> createPet(@Valid @RequestBody CreatePetRequestDto requestDto){
        CreatePetResponseDto responseDto =
                petService.createPet(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }
}
