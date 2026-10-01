CREATE TABLE public.pet_memberships
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL,
    pet_id     BIGINT      NOT NULL,

    role       VARCHAR(30) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pet_memberships_user
        FOREIGN KEY (user_id)
            REFERENCES public.users (id),

    CONSTRAINT fk_pet_memberships_pet
        FOREIGN KEY (pet_id)
            REFERENCES public.pets (id),

    CONSTRAINT uk_pet_memberships_user_pet
        UNIQUE (user_id, pet_id)
);