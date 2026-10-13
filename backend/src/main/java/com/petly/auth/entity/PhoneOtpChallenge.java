package com.petly.auth.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
@Entity
@Table(name = "phone_otp_challenges")
@Getter
@Setter
@NoArgsConstructor
public class PhoneOtpChallenge extends BaseEntity {

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    @Column(name = "code_hash", nullable = false, length = 255)
    private String codeHash;
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
    @Column(name = "attempts", nullable = false)
    private int attempts;
    @Column(name = "consumed_at")
    private Instant consumedAt;
}
