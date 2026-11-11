
-- Petly V19
-- Global user roles, pet shop memberships and store owner requests.
-- Existing records are preserved.

-- 1. Global system role
ALTER TABLE public.users
    ADD COLUMN system_role VARCHAR(20)
        NOT NULL DEFAULT 'USER';

ALTER TABLE public.users
    ADD CONSTRAINT chk_users_system_role
        CHECK (system_role IN ('USER', 'ADMIN'));


-- 2. Pet shop memberships
CREATE TABLE public.pet_shop_memberships (
                                             id BIGSERIAL PRIMARY KEY,

                                             user_id BIGINT NOT NULL
                                                 REFERENCES public.users(id) ON DELETE RESTRICT,

                                             pet_shop_id BIGINT NOT NULL
                                                 REFERENCES public.pet_shops(id) ON DELETE RESTRICT,

                                             role VARCHAR(20) NOT NULL
                                                 CHECK (role IN ('OWNER', 'MANAGER', 'STAFF')),

                                             is_active BOOLEAN NOT NULL DEFAULT TRUE,

                                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                             CONSTRAINT uq_pet_shop_membership
                                                 UNIQUE (user_id, pet_shop_id)
);

-- At most one active owner per shop.
CREATE UNIQUE INDEX uq_pet_shop_single_active_owner
    ON public.pet_shop_memberships (pet_shop_id)
    WHERE role = 'OWNER'
        AND is_active = TRUE;

CREATE INDEX idx_pet_shop_memberships_user_active
    ON public.pet_shop_memberships (user_id, is_active);

CREATE INDEX idx_pet_shop_memberships_shop_active
    ON public.pet_shop_memberships (pet_shop_id, is_active);


-- 3. Store owner requests
CREATE TABLE public.store_owner_requests (
                                             id BIGSERIAL PRIMARY KEY,

                                             user_id BIGINT NOT NULL
                                                 REFERENCES public.users(id) ON DELETE RESTRICT,

                                             shop_name VARCHAR(200) NOT NULL,
                                             city VARCHAR(100) NOT NULL DEFAULT 'Tehran',
                                             website_url TEXT,
                                             address TEXT,

                                             status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                                                 CHECK (
                                                         status IN ('PENDING', 'APPROVED', 'REJECTED')
                                                     ),

                                             reviewed_by_user_id BIGINT
                                                 REFERENCES public.users(id) ON DELETE RESTRICT,

                                             reviewed_at TIMESTAMPTZ,
                                             rejection_reason TEXT,

                                             pet_shop_id BIGINT
                                                 REFERENCES public.pet_shops(id) ON DELETE RESTRICT,

                                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                             CONSTRAINT chk_store_owner_request_review
                                                 CHECK (
                                                         (
                                                                     status = 'PENDING'
                                                                 AND reviewed_by_user_id IS NULL
                                                                 AND reviewed_at IS NULL
                                                                 AND pet_shop_id IS NULL
                                                             )
                                                         OR
                                                         (
                                                                     status = 'APPROVED'
                                                                 AND reviewed_by_user_id IS NOT NULL
                                                                 AND reviewed_at IS NOT NULL
                                                                 AND pet_shop_id IS NOT NULL
                                                             )
                                                         OR
                                                         (
                                                                     status = 'REJECTED'
                                                                 AND reviewed_by_user_id IS NOT NULL
                                                                 AND reviewed_at IS NOT NULL
                                                                 AND pet_shop_id IS NULL
                                                             )
                                                     ),

                                             CONSTRAINT uq_store_owner_request_pet_shop
                                                 UNIQUE (pet_shop_id)
);

-- Prevent duplicate pending applications for the same
-- shop name and city, regardless of applicant.
CREATE UNIQUE INDEX uq_store_owner_pending_shop
    ON public.store_owner_requests (
                                    LOWER(shop_name),
                                    LOWER(city)
        )
    WHERE status = 'PENDING';

CREATE INDEX idx_store_owner_requests_status_created
    ON public.store_owner_requests (status, created_at DESC);

CREATE INDEX idx_store_owner_requests_user_created
    ON public.store_owner_requests (user_id, created_at DESC);
