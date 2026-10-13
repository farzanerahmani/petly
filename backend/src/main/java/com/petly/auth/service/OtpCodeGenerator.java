package com.petly.auth.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Component

public class OtpCodeGenerator {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_BOUND = 1_000_000;
    private final SecureRandom secureRandom = new SecureRandom();

    public String generate() {
        int code = secureRandom.nextInt(OTP_BOUND);
        return String.format("%0" + OTP_LENGTH + "d", code);
    }
}
