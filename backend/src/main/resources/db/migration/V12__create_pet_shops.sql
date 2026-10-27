CREATE TABLE pet_shops (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL DEFAULT 'Tehran',
    website_url TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pet_shops_city_active
    ON pet_shops (city, is_active);

ALTER TABLE food_prices
    ADD COLUMN pet_shop_id BIGINT
        REFERENCES pet_shops (id) ON DELETE SET NULL;

CREATE INDEX idx_food_prices_shop_checked
    ON food_prices (pet_shop_id, checked_at DESC);
