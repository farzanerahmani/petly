package com.petly.auth.repository;

import com.petly.auth.entity.PhoneOtpChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
public interface PhoneOtpChallengeRepository extends JpaRepository<PhoneOtpChallenge, Long> {

    @Query(
            value = """
                    SELECT *
                    FROM phone_otp_challenges
                    WHERE phone_number = :phoneNumber
                      AND consumed_at IS NULL
                    ORDER BY created_at DESC
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    Optional<PhoneOtpChallenge> findLatestUnconsumedForUpdate(
            @Param("phoneNumber") String phoneNumber
    );

    @Modifying
    @Query("""
            UPDATE PhoneOtpChallenge challenge
            SET challenge.attempts = challenge.attempts + 1
            WHERE challenge.id = :id
              AND challenge.consumedAt IS NULL
              AND challenge.attempts < :maxAttempts
            """)
    int incrementAttemptsIfAllowed(
            @Param("id") Long id,
            @Param("maxAttempts") int maxAttempts
    );

    @Modifying
    @Query("""
    UPDATE PhoneOtpChallenge challenge
    SET challenge.consumedAt = :now
    WHERE challenge.phoneNumber = :phoneNumber
      AND challenge.consumedAt IS NULL
    """)
    int invalidatePreviousChallenges(
            @Param("phoneNumber") String phoneNumber,
            @Param("now") Instant now
    );
}
