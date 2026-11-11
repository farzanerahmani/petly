package com.petly.food.service;

import com.petly.food.controller.dto.PetShopMemberCreateRequest;
import com.petly.food.controller.dto.PetShopMemberResponse;
import com.petly.food.controller.dto.PetShopResponse;
import com.petly.food.controller.dto.PetShopUpdateRequest;
import com.petly.food.entity.PetShop;
import com.petly.food.entity.PetShopMembership;
import com.petly.food.entity.enums.PetShopMemberRole;
import com.petly.food.repository.PetShopMembershipRepository;
import com.petly.food.repository.PetShopRepository;
import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author farzane.rahmani
 * @created 11/8/2026
 */
@Service
@RequiredArgsConstructor
public class PetShopMembershipService {

    private final PetShopMembershipRepository membershipRepository;
    private final PetShopRepository petShopRepository;
    private final UserRepository userRepository;
    private final PetShopService petShopService;

    @Transactional
    public PetShopMemberResponse addMember(
            Long ownerId,
            Long petShopId,
            PetShopMemberCreateRequest request
    ) {
        verifyOwner(ownerId, petShopId);

        if (request.getRole() != PetShopMemberRole.MANAGER
                && request.getRole() != PetShopMemberRole.STAFF) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only MANAGER or STAFF can be assigned"
            );
        }

        User user = userRepository.findByPhoneNumber(
                request.getPhoneNumber().trim()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
        ));

        PetShop shop = petShopRepository.findActiveByIdForUpdate(petShopId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Active pet shop not found"
                ));

        PetShopMembership membership = membershipRepository
                .findByUserIdAndPetShopId(user.getId(), petShopId)
                .orElseGet(() -> {
                    PetShopMembership newMembership = new PetShopMembership();
                    newMembership.setUser(user);
                    newMembership.setPetShop(shop);
                    return newMembership;
                });
        if (membership.getRole() == PetShopMemberRole.OWNER) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Owner membership cannot be assigned as MANAGER or STAFF"
            );
        }

        if (membership.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User is already an active member"
            );
        }

        membership.setRole(request.getRole());
        membership.setActive(true);

        return toResponse(membershipRepository.save(membership));
    }

    @Transactional(readOnly = true)
    public List<PetShopMemberResponse> getMembers(
            Long ownerId,
            Long petShopId
    ) {
        verifyOwner(ownerId, petShopId);

        return membershipRepository
                .findAllByPetShopIdAndActiveTrueOrderByCreatedAtDesc(petShopId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deactivateMember(
            Long ownerId,
            Long petShopId,
            Long membershipId
    ) {
        verifyOwner(ownerId, petShopId);

        PetShopMembership membership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Membership not found"
                ));

        if (!membership.getPetShop().getId().equals(petShopId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Membership not found in this shop"
            );
        }

        if (membership.getRole() == PetShopMemberRole.OWNER) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Owner membership cannot be deactivated here"
            );
        }

        if (!membership.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Membership is already inactive"
            );
        }

        membership.setActive(false);
        membershipRepository.save(membership);
    }

    private void verifyOwner(Long userId, Long petShopId) {

        boolean isOwner = membershipRepository
                .findByUserIdAndPetShopIdAndActiveTrue(userId, petShopId)
                .map(membership ->
                        membership.getRole() == PetShopMemberRole.OWNER
                                && membership.getPetShop().isActive()
                )
                .orElse(false);

        if (!isOwner) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only the active owner of an active shop can perform this action"
            );
        }
    }

    private PetShopMemberResponse toResponse(PetShopMembership membership) {
        User user = membership.getUser();

        return new PetShopMemberResponse(
                membership.getId(),
                user.getId(),
                user.getPhoneNumber(),
                membership.getRole(),
                membership.isActive(),
                membership.getCreatedAt()
        );
    }

    @Transactional
    public PetShopMemberResponse updateMemberRole(
            Long ownerId,
            Long petShopId,
            Long membershipId,
            PetShopMemberRole newRole
    ) {
        verifyOwner(ownerId, petShopId);

        if (newRole != PetShopMemberRole.MANAGER
                && newRole != PetShopMemberRole.STAFF) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only MANAGER or STAFF roles are allowed"
            );
        }

        PetShopMembership membership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Membership not found"
                ));

        if (!membership.getPetShop().getId().equals(petShopId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Membership not found"
            );
        }

        if (membership.getRole() == PetShopMemberRole.OWNER) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Owner role cannot be changed"
            );
        }

        if (!membership.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Inactive membership cannot be updated"
            );
        }

        membership.setRole(newRole);

        return toResponse(membershipRepository.save(membership));
    }


    @Transactional
    public PetShopResponse updateShopInfo(
            Long ownerId,
            Long petShopId,
            PetShopUpdateRequest request
    ) {
        verifyOwner(ownerId, petShopId);

        PetShop shop = petShopService.updatePetShop(petShopId, request);

        return PetShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .city(shop.getCity())
                .websiteUrl(shop.getWebsiteUrl())
                .address(shop.getAddress())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .supportsDelivery(shop.isSupportsDelivery())
                .deliveryNotes(shop.getDeliveryNotes())
                .active(shop.isActive())
                .build();
    }

    @Transactional(readOnly = true)
    public PetShopResponse getShopInfo(
            Long ownerId,
            Long petShopId
    ) {
        verifyOwner(ownerId, petShopId);

        PetShop shop = petShopRepository.findById(petShopId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pet shop not found."
                        )
                );
        return PetShopResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .city(shop.getCity())
                .websiteUrl(shop.getWebsiteUrl())
                .address(shop.getAddress())
                .latitude(shop.getLatitude())
                .longitude(shop.getLongitude())
                .supportsDelivery(shop.isSupportsDelivery())
                .deliveryNotes(shop.getDeliveryNotes())
                .active(shop.isActive())
                .build();
    }

}

