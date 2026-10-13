package com.petly.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Getter
@Setter
public class VerifyOtpRequestDto {

    @NotBlank
    @Pattern(
            regexp = "^\\+[1-9]\\d{7,14}$",
            message = "Phone number must use international format"
    )
    private String phoneNumber;

    @NotBlank
    @Pattern(
            regexp = "^\\d{6}$",
            message = "OTP must be exactly 6 digits"
    )
    private String code;
}
