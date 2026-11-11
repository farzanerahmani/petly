package com.petly.food.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


/**
 * @author farzane.rahmani
 * @created 11/11/2026
 */
@Constraint(validatedBy = DeliverySettingsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDeliverySettings {

    String message() default
            "Delivery notes cannot be provided when delivery is disabled.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
