package com.petly.food.controller;


import com.petly.food.controller.dto.PetShopCreateRequest;
import com.petly.food.controller.dto.PetShopResponse;
import com.petly.food.controller.dto.PetShopUpdateRequest;
import com.petly.food.entity.PetShop;
import com.petly.food.service.PetShopMembershipService;
import com.petly.food.service.PetShopService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * @author farzane.rahmani
 * @created 11/3/2026
 */
@RestController
@RequestMapping("/api/v1/pet-shops")
public class PetShopController {

    private final PetShopService petShopService;

    private final PetShopMembershipService petShopMembershipService;


    public PetShopController(PetShopService petShopService,
                             PetShopMembershipService petShopMembershipService) {
        this.petShopService = petShopService;
        this.petShopMembershipService = petShopMembershipService;
    }

    @PostMapping
    public ResponseEntity<PetShopResponse> createPetShop(
            @Valid @RequestBody PetShopCreateRequest request
    ) {
        PetShopResponse petShop = petShopService.createPetShop(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petShop);
    }

    @GetMapping("/{petShopId}")
    public PetShopResponse getPetShop(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long petShopId
    ) {
        Long ownerId = Long.valueOf(jwt.getSubject());

        return petShopMembershipService.getShopInfo(
                ownerId,
                petShopId
        );
    }

    @PutMapping("/{petShopId}")
    public PetShopResponse updatePetShop(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long petShopId,
            @Valid @RequestBody PetShopUpdateRequest request
    ) {
        Long ownerId = Long.valueOf(jwt.getSubject());

        return petShopMembershipService.updateShopInfo(
                ownerId,
                petShopId,
                request
        );
    }
}
