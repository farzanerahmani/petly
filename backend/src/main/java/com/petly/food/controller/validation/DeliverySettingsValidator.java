package com.petly.food.controller.validation;

import com.petly.food.controller.dto.PetShopUpdateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author farzane.rahmani
 * @created 11/11/2026
 */
public class DeliverySettingsValidator
        implements ConstraintValidator<ValidDeliverySettings, PetShopUpdateRequest> {

    @Override
    public boolean isValid(
            PetShopUpdateRequest request,
            ConstraintValidatorContext context
    ) {
        if (request == null) {
            return true;
        }

        Boolean supportsDelivery = request.getSupportsDelivery();
        String deliveryNotes = request.getDeliveryNotes();

        if (Boolean.FALSE.equals(supportsDelivery)
                && deliveryNotes != null
                && !deliveryNotes.isBlank()) {
            return false;
        }

        return true;
    }
}
