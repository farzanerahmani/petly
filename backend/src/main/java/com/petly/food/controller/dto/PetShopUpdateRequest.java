package com.petly.food.controller.dto;

import com.petly.food.controller.validation.ValidCoordinates;
import com.petly.food.controller.validation.ValidDeliverySettings;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author farzane.rahmani
 * @created 11/10/2026
 */
@ValidCoordinates
@ValidDeliverySettings
@Getter
@Setter
public class PetShopUpdateRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @Size(max = 2048)
    @URL(protocol = "http,https")
    private String websiteUrl;

    @Size(max = 1000)
    private String address;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private BigDecimal latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private BigDecimal longitude;

    @NotNull
    private Boolean supportsDelivery;

    @Size(max = 2000)
    private String deliveryNotes;
}

