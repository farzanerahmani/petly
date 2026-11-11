-- =========================================
-- 0. Add pet shop information fields
-- =========================================

ALTER TABLE public.pet_shops
    ADD COLUMN address TEXT,
    ADD COLUMN latitude NUMERIC(10, 7),
    ADD COLUMN longitude NUMERIC(10, 7),
    ADD COLUMN supports_delivery BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN delivery_notes TEXT;
-- =========================================
-- 1. Add system role to users
-- =========================================

ALTER TABLE public.users
    ADD COLUMN system_role VARCHAR(20) NOT NULL DEFAULT 'USER';

ALTER TABLE public.users
    ADD CONSTRAINT chk_users_system_role
        CHECK (system_role IN ('USER', 'ADMIN'));


-- =========================================
-- 2. Pet shop memberships
-- =========================================

CREATE TABLE public.pet_shop_memberships (
                                             id BIGSERIAL PRIMARY KEY,

                                             user_id BIGINT NOT NULL,
                                             pet_shop_id BIGINT NOT NULL,

                                             role VARCHAR(20) NOT NULL,
                                             is_active BOOLEAN NOT NULL DEFAULT TRUE,

                                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                             CONSTRAINT fk_pet_shop_memberships_user
                                                 FOREIGN KEY (user_id)
                                                     REFERENCES public.users(id)
                                                     ON DELETE RESTRICT,

                                             CONSTRAINT fk_pet_shop_memberships_pet_shop
                                                 FOREIGN KEY (pet_shop_id)
                                                     REFERENCES public.pet_shops(id)
                                                     ON DELETE RESTRICT,

                                             CONSTRAINT uq_pet_shop_membership
                                                 UNIQUE (user_id, pet_shop_id),

                                             CONSTRAINT chk_pet_shop_membership_role
                                                 CHECK (role IN ('OWNER', 'MANAGER', 'STAFF'))
);


-- فقط یک مالک فعال برای هر فروشگاه
CREATE UNIQUE INDEX uq_pet_shop_single_active_owner
    ON public.pet_shop_memberships (pet_shop_id)
    WHERE role = 'OWNER' AND is_active = TRUE;

CREATE INDEX idx_pet_shop_memberships_user_active
    ON public.pet_shop_memberships (user_id, is_active);

CREATE INDEX idx_pet_shop_memberships_shop_active
    ON public.pet_shop_memberships (pet_shop_id, is_active);


-- =========================================
-- 3. Store owner requests
-- =========================================

CREATE TABLE public.store_owner_requests (
                                             id BIGSERIAL PRIMARY KEY,

                                             user_id BIGINT NOT NULL,

                                             shop_name VARCHAR(200) NOT NULL,
                                             city VARCHAR(100) NOT NULL DEFAULT 'Tehran',

                                             website_url TEXT,
                                             address TEXT,

                                             status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                                             reviewed_by_user_id BIGINT,
                                             reviewed_at TIMESTAMPTZ,

                                             rejection_reason TEXT,
                                             pet_shop_id BIGINT,

                                             created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                             CONSTRAINT fk_store_owner_requests_user
                                                 FOREIGN KEY (user_id)
                                                     REFERENCES public.users(id)
                                                     ON DELETE RESTRICT,

                                             CONSTRAINT fk_store_owner_requests_reviewer
                                                 FOREIGN KEY (reviewed_by_user_id)
                                                     REFERENCES public.users(id)
                                                     ON DELETE RESTRICT,

                                             CONSTRAINT fk_store_owner_requests_pet_shop
                                                 FOREIGN KEY (pet_shop_id)
                                                     REFERENCES public.pet_shops(id)
                                                     ON DELETE RESTRICT,

                                             CONSTRAINT chk_store_owner_request_status
                                                 CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),

                                             CONSTRAINT chk_store_owner_request_review
                                                 CHECK (
                                                         (
                                                                     status = 'PENDING'
                                                                 AND reviewed_by_user_id IS NULL
                                                                 AND reviewed_at IS NULL
                                                                 AND pet_shop_id IS NULL
                                                                 AND rejection_reason IS NULL
                                                             )
                                                         OR
                                                         (
                                                                     status = 'APPROVED'
                                                                 AND reviewed_by_user_id IS NOT NULL
                                                                 AND reviewed_at IS NOT NULL
                                                                 AND pet_shop_id IS NOT NULL
                                                                 AND rejection_reason IS NULL
                                                             )
                                                         OR
                                                         (
                                                                     status = 'REJECTED'
                                                                 AND reviewed_by_user_id IS NOT NULL
                                                                 AND reviewed_at IS NOT NULL
                                                                 AND pet_shop_id IS NULL
                                                                 AND rejection_reason IS NOT NULL
                                                                 AND LENGTH(TRIM(rejection_reason)) > 0
                                                             )
                                                     )
);


-- جلوگیری از درخواست تکراری PENDING
-- هر کاربر می‌تواند برای فروشگاه‌های متفاوت درخواست داشته باشد.
CREATE UNIQUE INDEX uq_store_owner_pending_shop
    ON public.store_owner_requests (
                                    user_id,
                                    LOWER(shop_name),
                                    LOWER(city)
        )
    WHERE status = 'PENDING';

CREATE INDEX idx_store_owner_requests_status_created
    ON public.store_owner_requests (status, created_at);

CREATE INDEX idx_store_owner_requests_user_created
    ON public.store_owner_requests (user_id, created_at);