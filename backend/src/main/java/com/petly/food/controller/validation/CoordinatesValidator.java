package com.petly.food.controller.validation;

import com.petly.food.controller.dto.PetShopUpdateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author farzane.rahmani
 * @created 11/11/2026
 */
public class CoordinatesValidator
        implements ConstraintValidator<ValidCoordinates, PetShopUpdateRequest> {

    @Override
    public boolean isValid(
            PetShopUpdateRequest request,
            ConstraintValidatorContext context
    ) {
        if (request == null) {
            return true;
        }

        boolean hasLatitude = request.getLatitude() != null;
        boolean hasLongitude = request.getLongitude() != null;

        return hasLatitude == hasLongitude;
    }
}
