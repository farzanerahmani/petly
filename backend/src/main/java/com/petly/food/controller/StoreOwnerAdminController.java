package com.petly.food.controller;

import com.petly.food.controller.dto.StoreOwnerAdminRequestResponse;
import com.petly.food.controller.dto.StoreOwnerReviewRequest;
import com.petly.food.service.StoreOwnerAdminService;
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
@RestController
@RequestMapping("/api/v1/admin/store-owner-requests")
@RequiredArgsConstructor
public class StoreOwnerAdminController {

    private final StoreOwnerAdminService adminService;

    @GetMapping
    public List<StoreOwnerAdminRequestResponse> getPendingRequests(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long adminId = Long.valueOf(jwt.getSubject());

        return adminService.getPendingRequests(adminId);
    }

    @PostMapping("/{requestId}/review")
    @ResponseStatus(HttpStatus.OK)
    public StoreOwnerAdminRequestResponse reviewRequest(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long requestId,
            @Valid @RequestBody StoreOwnerReviewRequest review
    ) {
        Long adminId = Long.valueOf(jwt.getSubject());

        return adminService.reviewRequest(
                adminId,
                requestId,
                review
        );
    }
}

