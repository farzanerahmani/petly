package com.petly.auth.service;

import com.petly.auth.entity.PhoneOtpChallenge;
import com.petly.auth.repository.PhoneOtpChallengeRepository;
import com.petly.common.exception.InvalidOtpException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Service
@RequiredArgsConstructor
public class OtpAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private final PhoneOtpChallengeRepository otpRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean recordFailedAttempt(Long challengeId) {
        return otpRepository.incrementAttemptsIfAllowed(challengeId, MAX_ATTEMPTS) == 1;
    }
}