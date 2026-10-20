CREATE TABLE pet_calorie_records
(
    id             BIGSERIAL PRIMARY KEY,
    pet_id         BIGINT         NOT NULL,
    weight_kg      NUMERIC(7, 2)  NOT NULL CHECK (weight_kg > 0),
    age_months     INTEGER        NOT NULL CHECK (age_months >= 0),
    activity_level VARCHAR(255)   NOT NULL,
    neutered       BOOLEAN        NOT NULL,
    rer_kcal       NUMERIC(10, 2) NOT NULL CHECK (rer_kcal > 0),
    factor         NUMERIC(5, 2)  NOT NULL CHECK (factor > 0),
    mer_kcal       NUMERIC(10, 2) NOT NULL CHECK (mer_kcal > 0),
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pet_calorie_records_pet FOREIGN KEY (pet_id) REFERENCES pets (id)
);

CREATE INDEX idx_pet_calorie_records_pet_date ON pet_calorie_records (pet_id, created_at DESC);
