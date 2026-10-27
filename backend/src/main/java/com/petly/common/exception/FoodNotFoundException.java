package com.petly.common.exception;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
public class FoodNotFoundException extends RuntimeException {

    public FoodNotFoundException(String message) {
        super(message);
    }
}
