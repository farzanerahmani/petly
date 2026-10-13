package com.petly.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Getter
@AllArgsConstructor
public class VerifyOtpResponseDto {

    private Long userId;
    private String phoneNumber;
    private boolean newUser;
    private String accessToken;
}
