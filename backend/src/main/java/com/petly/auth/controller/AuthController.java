package com.petly.auth.controller;

import com.petly.auth.controller.dto.RequestOtpRequestDto;
import com.petly.auth.controller.dto.RequestOtpResponseDto;
import com.petly.auth.service.OtpService;
import com.petly.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.petly.auth.controller.dto.VerifyOtpRequestDto;
import com.petly.auth.controller.dto.VerifyOtpResponseDto;
import com.petly.auth.service.OtpVerificationService;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@RestController
@RequestMapping("/api/v1/auth/otp")
@RequiredArgsConstructor
public class AuthController {

    private final OtpService otpService;

    private final OtpVerificationService otpVerificationService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<RequestOtpResponseDto>> requestOtp(
            @Valid @RequestBody RequestOtpRequestDto request
    ) {
        RequestOtpResponseDto response = otpService.requestOtp(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyOtpResponseDto>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequestDto request
    ) {
        VerifyOtpResponseDto response = otpVerificationService.verify(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
