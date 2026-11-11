package com.petly.food.controller;

import com.petly.food.controller.dto.StoreOwnerRequestCreateRequest;
import com.petly.food.controller.dto.StoreOwnerRequestResponse;
import com.petly.food.service.StoreOwnerRequestService;
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
@RequestMapping("/api/v1/store-owner-requests")
@RequiredArgsConstructor
public class StoreOwnerRequestController {

    private final StoreOwnerRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreOwnerRequestResponse createRequest(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody StoreOwnerRequestCreateRequest request
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        return requestService.createRequest(userId, request);
    }

    @GetMapping("/my")
    public List<StoreOwnerRequestResponse> getMyRequests(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        return requestService.getMyRequests(userId);
    }
}

