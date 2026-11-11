-- User notifications and Expo device tokens.

CREATE TABLE user_notifications
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT       NOT NULL
        REFERENCES users (id) ON DELETE CASCADE,

    notification_type VARCHAR(40)  NOT NULL
        CHECK (
                notification_type IN (
                                      'FOOD_PRICE_DROP',
                                      'FOOD_BACK_IN_STOCK',
                                      'WATCHLIST_OFFER',
                                      'SYSTEM'
                )
            ),

    title             VARCHAR(200) NOT NULL,
    body              TEXT         NOT NULL,
    data              JSONB,

    -- Allows deduplication of the same event for the same user.
    dedupe_key        VARCHAR(300) NOT NULL,

    read_at           TIMESTAMPTZ,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_user_notifications_dedupe
        UNIQUE (user_id, dedupe_key)
);

CREATE INDEX idx_user_notifications_user_created
    ON user_notifications (user_id, created_at DESC);

CREATE INDEX idx_user_notifications_unread
    ON user_notifications (user_id, created_at DESC)
    WHERE read_at IS NULL;


CREATE TABLE push_tokens
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL
        REFERENCES users (id) ON DELETE CASCADE,

    expo_push_token VARCHAR(255) NOT NULL UNIQUE,
    platform        VARCHAR(20)  NOT NULL
        CHECK (platform IN ('IOS', 'ANDROID')),

    device_name     VARCHAR(150),
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE,
    last_seen_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    created_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_push_tokens_user_active
    ON push_tokens (user_id, is_active);


CREATE TABLE user_food_watchlist
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT      NOT NULL
        REFERENCES users (id) ON DELETE CASCADE,

    food_product_id BIGINT      NOT NULL
        REFERENCES food_products (id) ON DELETE CASCADE,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_user_food_watchlist
        UNIQUE (user_id, food_product_id)
);

CREATE INDEX idx_user_food_watchlist_product
    ON user_food_watchlist (food_product_id);
