-- Add store location and delivery information
ALTER TABLE pet_shops
    ADD COLUMN address TEXT,
    ADD COLUMN latitude NUMERIC(10, 7),
    ADD COLUMN longitude NUMERIC(10, 7),
    ADD COLUMN supports_delivery BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN delivery_notes TEXT;

ALTER TABLE pet_shops
    ADD CONSTRAINT chk_pet_shops_latitude
        CHECK (latitude IS NULL OR latitude BETWEEN -90 AND 90),
    ADD CONSTRAINT chk_pet_shops_longitude
        CHECK (longitude IS NULL OR longitude BETWEEN -180 AND 180);

-- Add current offer and stock information
ALTER TABLE food_prices
    ADD COLUMN is_available BOOLEAN,
    ADD COLUMN stock_checked_at TIMESTAMPTZ,
    ADD COLUMN last_verified_at TIMESTAMPTZ;

CREATE INDEX idx_food_prices_available
    ON food_prices (food_product_id, is_available, checked_at DESC);

-- Payment providers
CREATE TABLE payment_providers (
                                   id BIGSERIAL PRIMARY KEY,
                                   code VARCHAR(50) NOT NULL UNIQUE,
                                   name VARCHAR(100) NOT NULL,
                                   website_url TEXT,
                                   is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                   created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Payment methods supported by each store
CREATE TABLE store_payment_methods (
                                       id BIGSERIAL PRIMARY KEY,
                                       pet_shop_id BIGINT NOT NULL
                                           REFERENCES pet_shops (id) ON DELETE CASCADE,
                                       payment_provider_id BIGINT NOT NULL
                                           REFERENCES payment_providers (id),
                                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                       terms_url TEXT,
                                       notes TEXT,
                                       verified_at TIMESTAMPTZ,
                                       created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT uq_store_payment_provider
                                           UNIQUE (pet_shop_id, payment_provider_id)
);

CREATE INDEX idx_store_payment_methods_active
    ON store_payment_methods (pet_shop_id, is_active);

-- Initial payment providers; store support must be verified separately
INSERT INTO payment_providers (code, name)
VALUES
    ('SNAPP_PAY', 'Snapp Pay'),
    ('DIGIPAY', 'DigiPay'),
    ('TOROB_PAY', 'Torob Pay'),
    ('TARA', 'Tara')
ON CONFLICT (code) DO NOTHING;