package com.petly.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestOtpResponseDto {

    private String phoneNumber;
    private Instant expiresAt;

    // فقط در محیط توسعه مقدار می‌گیرد.
    private String debugCode;
}
