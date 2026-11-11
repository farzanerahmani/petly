
-- DEMO DATA ONLY: prices and availability below are fictional.
-- Do not use these offers as real shopping information.

-- 1. Demo pet shops. Safe to run repeatedly.
INSERT INTO public.pet_shops (
    name,
    city,
    website_url,
    is_active,
    supports_delivery,
    delivery_notes
)
SELECT
    v.name,
    'Tehran',
    NULL,
    TRUE,
    v.supports_delivery,
    'DEMO DATA ONLY - delivery details are not verified'
FROM (
         VALUES
             ('Petly Demo Shop A', TRUE),
             ('Petly Demo Shop B', TRUE),
             ('Petly Demo Shop C', FALSE)
     ) AS v(name, supports_delivery)
WHERE NOT EXISTS (
        SELECT 1
        FROM public.pet_shops s
        WHERE s.name = v.name
          AND s.city = 'Tehran'
    );

-- 2. Demo payment methods.
-- Provider rows are seeded by V13. Only mark these as demo associations;
-- this does not confirm that any real shop supports these providers.
INSERT INTO public.store_payment_methods (
    pet_shop_id,
    payment_provider_id,
    is_active,
    notes
)
SELECT
    s.id,
    p.id,
    TRUE,
    'DEMO DATA ONLY - payment support is not verified'
FROM (
         VALUES
             ('Petly Demo Shop A', 'SNAPP_PAY'),
             ('Petly Demo Shop A', 'DIGIPAY'),
             ('Petly Demo Shop B', 'TARA'),
             ('Petly Demo Shop B', 'TOROB_PAY'),
             ('Petly Demo Shop C', 'DIGIPAY')
     ) AS demo(shop_name, provider_code)
         JOIN public.pet_shops s
              ON s.name = demo.shop_name
                  AND s.city = 'Tehran'
         JOIN public.payment_providers p
              ON p.code = demo.provider_code
WHERE NOT EXISTS (
        SELECT 1
        FROM public.store_payment_methods existing
        WHERE existing.pet_shop_id = s.id
          AND existing.payment_provider_id = p.id
    );

-- 3. Demo price offers for the two products seeded by V14.
-- Amounts are fictional IRR values. One offer is intentionally unavailable.
INSERT INTO public.food_prices (
    food_product_id,
    seller_name,
    price_amount,
    currency,
    package_weight_grams,
    product_url,
    pet_shop_id,
    is_available,
    stock_checked_at,
    last_verified_at
)
SELECT
    product.id,
    shop.name,
    demo.price_amount,
    'IRR',
    demo.package_weight_grams,
    NULL,
    shop.id,
    demo.is_available,
    NULL,
    NULL
FROM (
         VALUES
             (
                 'Pro Plan Performance 30/20 Chicken & Rice Formula',
                 'Purina Pro Plan',
                 'Petly Demo Shop A',
                 2500000.00::NUMERIC,
                 3000,
                 TRUE
             ),
             (
                 'Pro Plan Performance 30/20 Chicken & Rice Formula',
                 'Purina Pro Plan',
                 'Petly Demo Shop B',
                 2350000.00::NUMERIC,
                 3000,
                 TRUE
             ),
             (
                 'Pro Plan Performance 30/20 Chicken & Rice Formula',
                 'Purina Pro Plan',
                 'Petly Demo Shop C',
                 2600000.00::NUMERIC,
                 3000,
                 FALSE
             ),
             (
                 'Pro Plan Complete Essentials Natural Chicken & Egg Formula',
                 'Purina Pro Plan',
                 'Petly Demo Shop A',
                 1800000.00::NUMERIC,
                 1500,
                 TRUE
             ),
             (
                 'Pro Plan Complete Essentials Natural Chicken & Egg Formula',
                 'Purina Pro Plan',
                 'Petly Demo Shop B',
                 1950000.00::NUMERIC,
                 1500,
                 TRUE
             ),
             (
                 'Pro Plan Complete Essentials Natural Chicken & Egg Formula',
                 'Purina Pro Plan',
                 'Petly Demo Shop C',
                 1750000.00::NUMERIC,
                 1500,
                 TRUE
             )
     ) AS demo(
               product_name,
               brand_name,
               shop_name,
               price_amount,
               package_weight_grams,
               is_available
    )
         JOIN public.food_products product
              ON product.name = demo.product_name
                  AND product.brand = demo.brand_name
         JOIN public.pet_shops shop
              ON shop.name = demo.shop_name
                  AND shop.city = 'Tehran'
WHERE NOT EXISTS (
        SELECT 1
        FROM public.food_prices existing
        WHERE existing.food_product_id = product.id
          AND existing.pet_shop_id = shop.id
          AND existing.seller_name = shop.name
          AND existing.currency = 'IRR'
          AND existing.package_weight_grams = demo.package_weight_grams
    );
