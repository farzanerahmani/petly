package com.petly.auth.service;

import com.petly.auth.controller.dto.VerifyOtpRequestDto;
import com.petly.auth.controller.dto.VerifyOtpResponseDto;
import com.petly.auth.entity.PhoneOtpChallenge;
import com.petly.auth.repository.PhoneOtpChallengeRepository;
import com.petly.common.exception.InvalidOtpException;
import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Service
@RequiredArgsConstructor
public class OtpVerificationService {

    private static final int MAX_ATTEMPTS = 5;

    private final PhoneOtpChallengeRepository otpRepository;
    private final OtpHashService otpHashService;
    private final UserRepository userRepository;
    private final OtpAttemptService otpAttemptService;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public VerifyOtpResponseDto verify(VerifyOtpRequestDto request) {
        String phoneNumber = request.getPhoneNumber();

        PhoneOtpChallenge challenge = otpRepository
                .findLatestUnconsumedForUpdate(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid or expired OTP"
                ));

        if (!challenge.getExpiresAt().isAfter(Instant.now())) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        if (challenge.getAttempts() >= MAX_ATTEMPTS) {
            throw new InvalidOtpException();
        }

        if (!otpHashService.matches(
                phoneNumber,
                request.getCode(),
                challenge.getCodeHash()
        )) {
            otpAttemptService.recordFailedAttempt(challenge.getId());
            throw new InvalidOtpException();
        }

        challenge.setConsumedAt(Instant.now());
        otpRepository.save(challenge);


        Optional<User> existingUser = userRepository.findByPhoneNumber(phoneNumber);

        boolean newUser = existingUser.isEmpty();

        User user = existingUser.orElseGet(() -> {
            User createdUser = new User();
            createdUser.setPhoneNumber(phoneNumber);
            return userRepository.save(createdUser);
        });

        String accessToken = jwtTokenService.generateAccessToken(user.getId());
        return new VerifyOtpResponseDto(
                user.getId(),
                user.getPhoneNumber(),
                newUser,
                accessToken
        );
    }
}
