CREATE TABLE pet_body_condition_records
(
    id            BIGSERIAL PRIMARY KEY,
    pet_id        BIGINT      NOT NULL,
    score         SMALLINT    NOT NULL CHECK ( score BETWEEN 1 AND 9),
    recorded_date DATE        NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pet_body_condition_records_pet
        FOREIGN KEY (pet_id)
            REFERENCES public.pets (id)
);