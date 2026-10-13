package com.petly.auth.service;

import com.petly.auth.controller.dto.RequestOtpRequestDto;
import com.petly.auth.controller.dto.RequestOtpResponseDto;
import com.petly.auth.entity.PhoneOtpChallenge;
import com.petly.auth.repository.PhoneOtpChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Service
@RequiredArgsConstructor
public class OtpService {

    private final PhoneOtpChallengeRepository otpRepository;
    private final OtpCodeGenerator otpCodeGenerator;
    private final OtpHashService otpHashService;

    @Value("${petly.auth.otp.expose-code:false}")
    private boolean exposeCode;

    @Transactional
    public RequestOtpResponseDto requestOtp(RequestOtpRequestDto request) {
        String phoneNumber = request.getPhoneNumber();

        otpRepository.invalidatePreviousChallenges(phoneNumber, Instant.now());

        String code = otpCodeGenerator.generate();
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(300);

        PhoneOtpChallenge challenge = new PhoneOtpChallenge();
        challenge.setPhoneNumber(phoneNumber);
        challenge.setCodeHash(otpHashService.hash(phoneNumber, code));
        challenge.setExpiresAt(expiresAt);
        challenge.setAttempts(0);

        otpRepository.save(challenge);

        return new RequestOtpResponseDto(
                phoneNumber,
                expiresAt,
                exposeCode ? code : null
        );
    }
}
