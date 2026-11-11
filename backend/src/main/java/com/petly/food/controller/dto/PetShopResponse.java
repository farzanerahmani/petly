package com.petly.food.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author farzane.rahmani
 * @created 11/11/2026
 */
@Getter
@Builder
public class PetShopResponse {

    private Long id;
    private String name;
    private String city;
    private String websiteUrl;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean supportsDelivery;
    private String deliveryNotes;
    private Boolean active;
}
