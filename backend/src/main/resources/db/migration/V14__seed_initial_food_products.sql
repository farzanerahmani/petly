
-- Initial food catalog data.
-- No prices or store availability are fabricated.

INSERT INTO public.food_products (
    name,
    brand,
    species,
    food_type,
    life_stage,
    description,
    ingredients
)
SELECT
    'Pro Plan Performance 30/20 Chicken & Rice Formula',
    'Purina Pro Plan',
    'DOG',
    'DRY',
    'ALL_LIFE_STAGES',
    'Dry dog food. Check the manufacturer label for the exact regional formula and feeding guidance.',
    'Chicken, corn gluten meal, rice, beef fat, poultry by-product meal, whole grain corn, dried egg product, fish meal, fish oil, vitamins and minerals.'
WHERE NOT EXISTS (
        SELECT 1
        FROM public.food_products
        WHERE name = 'Pro Plan Performance 30/20 Chicken & Rice Formula'
          AND brand = 'Purina Pro Plan'
          AND species = 'DOG'
    );

INSERT INTO public.food_products (
    name,
    brand,
    species,
    food_type,
    life_stage,
    description,
    ingredients
)
SELECT
    'Pro Plan Complete Essentials Natural Chicken & Egg Formula',
    'Purina Pro Plan',
    'CAT',
    'DRY',
    'ADULT',
    'Dry cat food for adult maintenance. Check the manufacturer label for the exact regional formula and feeding guidance.',
    'Chicken, chicken meal, pea protein, dried egg product, cassava root flour, pea starch, potato protein, dried yeast, vitamins and minerals.'
WHERE NOT EXISTS (
        SELECT 1
        FROM public.food_products
        WHERE name = 'Pro Plan Complete Essentials Natural Chicken & Egg Formula'
          AND brand = 'Purina Pro Plan'
          AND species = 'CAT'
    );

-- Nutrition values are manufacturer-reported guaranteed analysis values.
-- Energy is intentionally NULL because the cited labels do not establish
-- a directly comparable energy value for these entries.
-- verified_at remains NULL: insertion does not mean a fresh verification.

INSERT INTO public.food_nutrition (
    food_product_id,
    energy_kcal_per_100g,
    protein_percent,
    fat_percent,
    fiber_percent,
    moisture_percent,
    source,
    verified_at
)
SELECT
    p.id,
    NULL,
    30.0,
    20.0,
    3.0,
    12.0,
    'https://www.purina.com/sites/default/files/product-label-deck-file/2023-05/4461-z446121-pro-plan-dog-food-performance-3020-chicken-rice-formula-l02.pdf',
    NULL
FROM public.food_products p
WHERE p.name = 'Pro Plan Performance 30/20 Chicken & Rice Formula'
  AND p.brand = 'Purina Pro Plan'
  AND p.species = 'DOG'
  AND NOT EXISTS (
        SELECT 1
        FROM public.food_nutrition n
        WHERE n.food_product_id = p.id
    );

INSERT INTO public.food_nutrition (
    food_product_id,
    energy_kcal_per_100g,
    protein_percent,
    fat_percent,
    fiber_percent,
    moisture_percent,
    source,
    verified_at
)
SELECT
    p.id,
    NULL,
    40.0,
    16.0,
    3.0,
    12.0,
    'https://www.purina.com/sites/default/files/products/files/4642_-_B464219_Pro_Plan_Cat_Food_Complete_Essentials_Natural_Chicken_Egg_Formula.pdf',
    NULL
FROM public.food_products p
WHERE p.name = 'Pro Plan Complete Essentials Natural Chicken & Egg Formula'
  AND p.brand = 'Purina Pro Plan'
  AND p.species = 'CAT'
  AND NOT EXISTS (
        SELECT 1
        FROM public.food_nutrition n
        WHERE n.food_product_id = p.id
    );
