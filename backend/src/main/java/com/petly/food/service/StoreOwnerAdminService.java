package com.petly.food.service;

import com.petly.food.controller.dto.StoreOwnerAdminRequestResponse;
import com.petly.food.controller.dto.StoreOwnerReviewRequest;
import com.petly.food.entity.*;
import com.petly.food.entity.enums.PetShopMemberRole;
import com.petly.food.entity.enums.StoreOwnerRequestStatus;
import com.petly.food.repository.PetShopMembershipRepository;
import com.petly.food.repository.PetShopRepository;
import com.petly.food.repository.StoreOwnerRequestRepository;
import com.petly.user.entity.User;
import com.petly.user.entity.enums.SystemRole;
import com.petly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 11/8/2026
 */
@Service
@RequiredArgsConstructor
public class StoreOwnerAdminService {

    private final StoreOwnerRequestRepository requestRepository;
    private final PetShopMembershipRepository membershipRepository;
    private final PetShopRepository petShopRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<StoreOwnerAdminRequestResponse> getPendingRequests(
            Long adminId
    ) {
        verifyAdmin(adminId);

        return requestRepository
                .findAllByStatusOrderByCreatedAtDesc(
                        StoreOwnerRequestStatus.PENDING
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public StoreOwnerAdminRequestResponse reviewRequest(
            Long adminId,
            Long requestId,
            StoreOwnerReviewRequest review
    ) {
        User admin = verifyAdmin(adminId);

        StoreOwnerRequest request = requestRepository
                .findByIdAndStatusForUpdate(
                        requestId,
                        StoreOwnerRequestStatus.PENDING
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pending request not found"
                ));

        if (Boolean.TRUE.equals(review.getApproved())) {

            PetShop petShop = resolvePetShop(request, review);

            boolean ownerExists = membershipRepository
                    .existsByPetShopIdAndRoleAndActiveTrue(
                            petShop.getId(),
                            PetShopMemberRole.OWNER
                    );

            if (ownerExists) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "This shop already has an active owner"
                );
            }

            createOrPromoteOwnerMembership(
                    request.getUser(),
                    petShop
            );

            request.setPetShop(petShop);
            request.setStatus(StoreOwnerRequestStatus.APPROVED);
            request.setRejectionReason(null);

        } else {

            if (review.getPetShopId() != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "A rejected request cannot have a shop assigned"
                );
            }

            if (review.getRejectionReason() == null
                    || review.getRejectionReason().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Rejection reason is required"
                );
            }

            request.setStatus(StoreOwnerRequestStatus.REJECTED);
            request.setRejectionReason(
                    review.getRejectionReason().trim()
            );
            request.setPetShop(null);
        }

        request.setReviewedBy(admin);
        request.setReviewedAt(Instant.now());

        return toResponse(requestRepository.save(request));
    }

    private PetShop resolvePetShop(
            StoreOwnerRequest request,
            StoreOwnerReviewRequest review
    ) {
        if (review.getPetShopId() != null) {
            return petShopRepository
                    .findActiveByIdForUpdate(review.getPetShopId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Active pet shop not found"
                    ));
        }

        String websiteUrl = request.getWebsiteUrl();

        if (websiteUrl != null && !websiteUrl.isBlank()
                && petShopRepository.findByWebsiteUrl(
                websiteUrl
        ).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A shop with this website already exists. Select its ID."
            );
        }

        PetShop petShop = new PetShop();
        petShop.setName(request.getShopName());
        petShop.setCity(request.getCity());
        petShop.setWebsiteUrl(websiteUrl);
        petShop.setAddress(request.getAddress());
        petShop.setActive(true);

        return petShopRepository.save(petShop);
    }

    private void createOrPromoteOwnerMembership(
            User user,
            PetShop petShop
    ) {
        PetShopMembership membership = membershipRepository
                .findByUserIdAndPetShopId(
                        user.getId(),
                        petShop.getId()
                )
                .orElseGet(() -> {
                    PetShopMembership newMembership =
                            new PetShopMembership();

                    newMembership.setUser(user);
                    newMembership.setPetShop(petShop);

                    return newMembership;
                });

        membership.setRole(PetShopMemberRole.OWNER);
        membership.setActive(true);

        membershipRepository.save(membership);
    }

    private User verifyAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Admin user not found"
                ));

        if (admin.getSystemRole() != SystemRole.ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin access required"
            );
        }

        return admin;
    }

    private StoreOwnerAdminRequestResponse toResponse(
            StoreOwnerRequest request
    ) {
        return new StoreOwnerAdminRequestResponse(
                request.getId(),
                request.getUser().getId(),
                request.getShopName(),
                request.getCity(),
                request.getWebsiteUrl(),
                request.getAddress(),
                request.getStatus(),
                request.getPetShop() != null
                        ? request.getPetShop().getId()
                        : null,
                request.getRejectionReason(),
                request.getCreatedAt(),
                request.getReviewedAt()
        );
    }
}

