package com.petly.food.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author farzane.rahmani
 * @created 11/11/2026
 */
@Constraint(validatedBy = CoordinatesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCoordinates {

    String message() default "Latitude and longitude must be provided together.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
