-- Petly V18: canonical package variants, shop listings,
-- scrape checks and match review.
-- Schema only: no business data is seeded, deleted or rewritten.

-- 1) Canonical package variants.
CREATE TABLE public.food_variants (
                                      id BIGSERIAL PRIMARY KEY,
                                      food_product_id BIGINT NOT NULL
                                          REFERENCES public.food_products (id) ON DELETE CASCADE,
                                      package_weight_grams INTEGER,
                                      weight_status VARCHAR(20) NOT NULL
                                          CHECK (weight_status IN (
                                                                   'CONFIRMED',
                                                                   'NEEDS_REVIEW',
                                                                   'MULTIPLE_WEIGHTS',
                                                                   'WEIGHT_UNKNOWN'
                                              )),
                                      created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                      CONSTRAINT chk_food_variants_weight
                                          CHECK (
                                                  package_weight_grams IS NULL
                                                  OR package_weight_grams > 0
                                              ),

                                      CONSTRAINT chk_food_variants_weight_status
                                          CHECK (
                                                  (
                                                          package_weight_grams IS NULL
                                                          AND weight_status IN (
                                                                                'MULTIPLE_WEIGHTS',
                                                                                'WEIGHT_UNKNOWN',
                                                                                'NEEDS_REVIEW'
                                                          )
                                                      )
                                                  OR
                                                  (
                                                          package_weight_grams IS NOT NULL
                                                          AND weight_status IN ('CONFIRMED', 'NEEDS_REVIEW')
                                                      )
                                              )
);

CREATE UNIQUE INDEX uq_food_variants_product_weight
    ON public.food_variants (food_product_id, package_weight_grams)
    WHERE package_weight_grams IS NOT NULL;

CREATE UNIQUE INDEX uq_food_variants_product_unknown_weight
    ON public.food_variants (food_product_id)
    WHERE package_weight_grams IS NULL;

CREATE INDEX idx_food_variants_product
    ON public.food_variants (food_product_id);


-- 2) Shop listings are independent from canonical product matching.
CREATE TABLE public.food_listings (
                                      id BIGSERIAL PRIMARY KEY,
                                      pet_shop_id BIGINT NOT NULL
                                          REFERENCES public.pet_shops (id) ON DELETE RESTRICT,

                                      food_variant_id BIGINT
                                          REFERENCES public.food_variants (id) ON DELETE RESTRICT,

                                      external_product_id VARCHAR(200),
                                      raw_product_name VARCHAR(500) NOT NULL,
                                      product_url TEXT NOT NULL,

                                      matching_status VARCHAR(24) NOT NULL DEFAULT 'UNMATCHED'
                                          CHECK (matching_status IN (
                                                                     'UNMATCHED',
                                                                     'NEEDS_REVIEW',
                                                                     'MANUAL_CONFIRMED'
                                              )),

                                      is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                      first_seen_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      last_seen_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                      CONSTRAINT chk_food_listings_confirmed_match
                                          CHECK (
                                                  (
                                                              matching_status = 'MANUAL_CONFIRMED'
                                                          AND food_variant_id IS NOT NULL
                                                      )
                                                  OR matching_status <> 'MANUAL_CONFIRMED'
                                              ),

                                      CONSTRAINT uq_food_listings_shop_url
                                          UNIQUE (pet_shop_id, product_url)
);

CREATE INDEX idx_food_listings_shop_active
    ON public.food_listings (pet_shop_id, is_active);

CREATE INDEX idx_food_listings_variant
    ON public.food_listings (food_variant_id);

CREATE INDEX idx_food_listings_matching_status
    ON public.food_listings (matching_status);


-- 3) Every scraper observation is retained.
CREATE TABLE public.food_listing_checks (
                                            id BIGSERIAL PRIMARY KEY,
                                            food_listing_id BIGINT NOT NULL
                                                REFERENCES public.food_listings (id) ON DELETE CASCADE,

                                            availability VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'
                                                CHECK (availability IN (
                                                                        'IN_STOCK',
                                                                        'OUT_OF_STOCK',
                                                                        'UNKNOWN'
                                                    )),

                                            stock_text VARCHAR(500),
                                            scrape_error TEXT,
                                            checked_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Required for the composite FK from food_prices.
                                            CONSTRAINT uq_food_listing_checks_id_listing
                                                UNIQUE (id, food_listing_id)
);

CREATE INDEX idx_food_listing_checks_listing_checked
    ON public.food_listing_checks (food_listing_id, checked_at DESC);


-- 4) Allow price observations before canonical product matching.
-- NULL price and currency mean unknown price; zero is never a sentinel.

ALTER TABLE public.food_prices
    ALTER COLUMN food_product_id DROP NOT NULL,
    ALTER COLUMN price_amount DROP NOT NULL,
    ALTER COLUMN currency DROP NOT NULL;

-- Remove legacy constraints before replacing them.
ALTER TABLE public.food_prices
    DROP CONSTRAINT food_prices_food_product_id_fkey,
    DROP CONSTRAINT food_prices_price_amount_check,
    DROP CONSTRAINT food_prices_currency_check;

-- Keep canonical product references protected while price history exists.
-- Deactivate products instead of physically deleting them.
ALTER TABLE public.food_prices
    ADD CONSTRAINT food_prices_food_product_id_fkey
        FOREIGN KEY (food_product_id)
            REFERENCES public.food_products (id)
            ON DELETE RESTRICT;

-- Add listing and scraper-check references.
ALTER TABLE public.food_prices
    ADD COLUMN food_listing_id BIGINT,
    ADD COLUMN listing_check_id BIGINT;

ALTER TABLE public.food_prices
    ADD CONSTRAINT fk_food_prices_listing
        FOREIGN KEY (food_listing_id)
            REFERENCES public.food_listings (id)
            ON DELETE RESTRICT,

    -- The check must belong to the same listing as the price.
    -- RESTRICT preserves the observation and prevents accidental
    -- deletion of a check referenced by a price record.
    ADD CONSTRAINT fk_food_prices_listing_check
        FOREIGN KEY (listing_check_id, food_listing_id)
            REFERENCES public.food_listing_checks (id, food_listing_id)
            ON DELETE RESTRICT,

    ADD CONSTRAINT chk_food_prices_product_or_listing
        CHECK (
                food_product_id IS NOT NULL
                OR food_listing_id IS NOT NULL
            ),

    ADD CONSTRAINT chk_food_prices_amount_currency_pair
        CHECK (
                (
                        price_amount IS NULL
                        AND currency IS NULL
                    )
                OR
                (
                        price_amount IS NOT NULL
                        AND price_amount > 0
                        AND currency IS NOT NULL
                        AND currency IN ('IRR', 'IRT')
                    )
            );

CREATE INDEX idx_food_prices_listing_checked
    ON public.food_prices (food_listing_id, checked_at DESC);

CREATE INDEX idx_food_prices_listing_check
    ON public.food_prices (listing_check_id);

CREATE UNIQUE INDEX uq_food_prices_listing_check
    ON public.food_prices (listing_check_id)
    WHERE listing_check_id IS NOT NULL;


-- 5) Candidate matches are suggestions only.
CREATE TABLE public.food_match_candidates (
                                              id BIGSERIAL PRIMARY KEY,

                                              food_listing_id BIGINT NOT NULL
                                                  REFERENCES public.food_listings (id) ON DELETE CASCADE,

                                              food_variant_id BIGINT NOT NULL
                                                  REFERENCES public.food_variants (id) ON DELETE CASCADE,

                                              confidence_score NUMERIC(5, 2)
                                                  CHECK (
                                                          confidence_score IS NULL
                                                          OR confidence_score BETWEEN 0 AND 100
                                                      ),

                                              match_reason TEXT,

                                              status VARCHAR(20) NOT NULL DEFAULT 'NEEDS_REVIEW'
                                                  CHECK (status IN (
                                                                    'NEEDS_REVIEW',
                                                                    'APPROVED',
                                                                    'REJECTED'
                                                      )),

                                              reviewed_at TIMESTAMPTZ,
                                              review_note TEXT,
                                              created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                              CONSTRAINT uq_food_match_candidate
                                                  UNIQUE (food_listing_id, food_variant_id)
);

CREATE INDEX idx_food_match_candidates_status
    ON public.food_match_candidates (status, created_at DESC);
