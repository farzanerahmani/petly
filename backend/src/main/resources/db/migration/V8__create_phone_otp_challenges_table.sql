CREATE TABLE phone_otp_challenges
(
    id              BIGSERIAL PRIMARY KEY,
    phone_number    VARCHAR(20) NOT NULL,
    code_hash       VARCHAR(255) NOT NULL,
    expires_at      TIMESTAMPTZ NOT NULL,
    attempts        INTEGER NOT NULL DEFAULT 0,
    consumed_at     TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_phone_otp_attempts
        CHECK (attempts >= 0)
);

CREATE INDEX idx_phone_otp_phone_created
    ON phone_otp_challenges (phone_number, created_at DESC);
