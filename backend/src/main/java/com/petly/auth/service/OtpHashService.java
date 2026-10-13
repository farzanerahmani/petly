package com.petly.auth.service;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Component
public class OtpHashService {
    private final String secret;

    public OtpHashService(@org.springframework.beans.factory.annotation.Value("${petly.auth.otp-hmac-secret}") String secret) {
        this.secret = secret;
    }

    public String hash(String phoneNumber, String code) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256");
            mac.init(key);
            byte[] input = (phoneNumber + ":" + code).getBytes(StandardCharsets.UTF_8);
            return Base64.getEncoder().encodeToString(mac.doFinal(input));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not hash OTP", exception);
        }
    }

    public boolean matches(String phoneNumber, String code, String expectedHash) {
        String actualHash = hash(phoneNumber, code);
        return MessageDigest.isEqual(actualHash.getBytes(StandardCharsets.UTF_8),
                expectedHash.getBytes(StandardCharsets.UTF_8));
    }
}
