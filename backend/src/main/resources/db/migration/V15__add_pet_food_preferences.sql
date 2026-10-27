ALTER TABLE pets
    ADD COLUMN food_type_preference VARCHAR(30)
        NOT NULL DEFAULT 'NO_PREFERENCE',
    ADD COLUMN current_food VARCHAR(200),
    ADD COLUMN current_food_satisfaction VARCHAR(20)
        NOT NULL DEFAULT 'UNKNOWN';