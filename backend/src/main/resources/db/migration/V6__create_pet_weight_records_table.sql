CREATE TABLE pet_weight_records
(
    id            BIGSERIAL PRIMARY KEY,
    pet_id        BIGINT        NOT NULL,
    weight_kg     NUMERIC(6, 2) NOT NULL CHECK ( weight_kg > 0 ), --we have not validation for negative value, for example we can insert
    -- -5.0 kg in data base , we must be decide that we do validating in service or here
    recorded_date DATE          NOT NULL,
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pet_weight_records_pet
        FOREIGN KEY (pet_id)
            REFERENCES public.pets (id)                           -- fk garante that we have not any weight record when we have not any pet
);