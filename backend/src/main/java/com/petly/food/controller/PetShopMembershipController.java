package com.petly.food.controller;

import com.petly.food.controller.dto.PetShopMemberCreateRequest;
import com.petly.food.controller.dto.PetShopMemberResponse;
import com.petly.food.entity.enums.PetShopMemberRole;
import com.petly.food.service.PetShopMembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 11/8/2026
 */
@RequestMapping("/api/v1/pet-shops/{petShopId}/members")
@RequiredArgsConstructor
public class PetShopMembershipController {

    private final PetShopMembershipService membershipService;

    @PatchMapping("/{membershipId}/role")
    public PetShopMemberResponse updateMemberRole(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long petShopId,
            @PathVariable Long membershipId,
            @RequestParam PetShopMemberRole role
    ) {
        Long ownerId = Long.valueOf(jwt.getSubject());

        return membershipService.updateMemberRole(
                ownerId,
                petShopId,
                membershipId,
                role
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetShopMemberResponse addMember(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long petShopId,
            @Valid @RequestBody PetShopMemberCreateRequest request
    ) {
        Long ownerId = Long.valueOf(jwt.getSubject());

        return membershipService.addMember(
                ownerId,
                petShopId,
                request
        );
    }

    @GetMapping
    public List<PetShopMemberResponse> getMembers(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long petShopId
    ) {
        Long ownerId = Long.valueOf(jwt.getSubject());

        return membershipService.getMembers(ownerId, petShopId);
    }

    @DeleteMapping("/{membershipId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateMember(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long petShopId,
            @PathVariable Long membershipId
    ) {
        Long ownerId = Long.valueOf(jwt.getSubject());

        membershipService.deactivateMember(
                ownerId,
                petShopId,
                membershipId
        );
    }
}

