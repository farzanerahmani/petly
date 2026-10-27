CREATE TABLE food_products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(150),
    species VARCHAR(20) NOT NULL
        CHECK (species IN ('DOG', 'CAT', 'BIRD', 'RABBIT', 'OTHER')),
    food_type VARCHAR(20) NOT NULL
        CHECK (food_type IN ('DRY', 'WET', 'OTHER')),
    life_stage VARCHAR(30)
        CHECK (
            life_stage IS NULL OR
            life_stage IN ('PUPPY', 'KITTEN', 'ADULT', 'SENIOR', 'ALL_LIFE_STAGES')
        ),
    description TEXT,
    ingredients TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_food_products_species_type_active
    ON food_products (species, food_type, is_active);

CREATE TABLE food_nutrition (
    id BIGSERIAL PRIMARY KEY,
    food_product_id BIGINT NOT NULL UNIQUE
        REFERENCES food_products (id) ON DELETE CASCADE,
    energy_kcal_per_100g NUMERIC(10, 2)
        CHECK (energy_kcal_per_100g IS NULL OR energy_kcal_per_100g > 0),
    protein_percent NUMERIC(5, 2)
        CHECK (protein_percent IS NULL OR protein_percent BETWEEN 0 AND 100),
    fat_percent NUMERIC(5, 2)
        CHECK (fat_percent IS NULL OR fat_percent BETWEEN 0 AND 100),
    fiber_percent NUMERIC(5, 2)
        CHECK (fiber_percent IS NULL OR fiber_percent BETWEEN 0 AND 100),
    moisture_percent NUMERIC(5, 2)
        CHECK (moisture_percent IS NULL OR moisture_percent BETWEEN 0 AND 100),
    source VARCHAR(500),
    verified_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE food_prices (
    id BIGSERIAL PRIMARY KEY,
    food_product_id BIGINT NOT NULL
        REFERENCES food_products (id) ON DELETE CASCADE,
    seller_name VARCHAR(200) NOT NULL,
    price_amount NUMERIC(14, 2) NOT NULL CHECK (price_amount > 0),
    currency VARCHAR(3) NOT NULL CHECK (currency IN ('IRR', 'IRT')),
    package_weight_grams INTEGER
        CHECK (package_weight_grams IS NULL OR package_weight_grams > 0),
    product_url TEXT,
    checked_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_food_prices_product_checked
    ON food_prices (food_product_id, checked_at DESC);
