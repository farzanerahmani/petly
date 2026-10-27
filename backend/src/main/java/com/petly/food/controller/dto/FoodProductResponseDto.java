package com.petly.food.controller.dto;

import com.petly.food.entity.FoodNutrition;
import com.petly.food.entity.FoodPrice;
import com.petly.food.entity.FoodProduct;
import com.petly.food.entity.enums.FoodCurrency;
import com.petly.food.entity.enums.FoodLifeStage;
import com.petly.food.entity.enums.FoodType;
import com.petly.pet.entity.enums.Species;
import com.petly.food.entity.StorePaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * @author farzane.rahmani
 * @created 10/20/2026
 */
@Getter
@Setter
@NoArgsConstructor
public class FoodProductResponseDto {

    private Long id;
    private String name;
    private String brand;
    private Species species;
    private FoodType foodType;
    private FoodLifeStage lifeStage;
    private String description;
    private String ingredients;
    private FoodNutritionDto nutrition;
    private List<FoodPriceDto> prices;

    public static FoodProductResponseDto fromEntity(FoodProduct product) {
        FoodProductResponseDto dto = new FoodProductResponseDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setSpecies(product.getSpecies());
        dto.setFoodType(product.getFoodType());
        dto.setLifeStage(product.getLifeStage());
        dto.setDescription(product.getDescription());
        dto.setIngredients(product.getIngredients());

        FoodNutrition nutrition = product.getNutrition();
        if (nutrition != null) {
            dto.setNutrition(FoodNutritionDto.fromEntity(nutrition));
        }

        List<FoodPriceDto> prices = product.getPrices()
                .stream()
                .map(FoodPriceDto::fromEntity)
                .toList();

        dto.setPrices(prices);

        return dto;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class FoodNutritionDto {
        private BigDecimal energyKcalPer100g;
        private BigDecimal proteinPercent;
        private BigDecimal fatPercent;
        private BigDecimal fiberPercent;
        private BigDecimal moisturePercent;
        private String source;
        private Instant verifiedAt;

        public static FoodNutritionDto fromEntity(FoodNutrition nutrition) {
            FoodNutritionDto dto = new FoodNutritionDto();
            dto.setEnergyKcalPer100g(nutrition.getEnergyKcalPer100g());
            dto.setProteinPercent(nutrition.getProteinPercent());
            dto.setFatPercent(nutrition.getFatPercent());
            dto.setFiberPercent(nutrition.getFiberPercent());
            dto.setMoisturePercent(nutrition.getMoisturePercent());
            dto.setSource(nutrition.getSource());
            dto.setVerifiedAt(nutrition.getVerifiedAt());
            return dto;
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class FoodPriceDto {
        private Long id;
        private String sellerName;
        private BigDecimal priceAmount;
        private FoodCurrency currency;
        private Integer packageWeightGrams;
        private String productUrl;
        private Instant checkedAt;

        private Long petShopId;
        private String petShopName;
        private String city;
        private String storeAddress;
        private Boolean available;
        private Instant stockCheckedAt;
        private Instant lastVerifiedAt;
        private List<PaymentMethodDto> paymentMethods;

        public static FoodPriceDto fromEntity(FoodPrice price) {
            FoodPriceDto dto = new FoodPriceDto();

            dto.setId(price.getId());
            dto.setSellerName(price.getSellerName());
            dto.setPriceAmount(price.getPriceAmount());
            dto.setCurrency(price.getCurrency());
            dto.setPackageWeightGrams(price.getPackageWeightGrams());
            dto.setProductUrl(price.getProductUrl());
            dto.setCheckedAt(price.getCheckedAt());

            dto.setAvailable(price.getAvailable());
            dto.setStockCheckedAt(price.getStockCheckedAt());
            dto.setLastVerifiedAt(price.getLastVerifiedAt());

            if (price.getPetShop() != null) {
                dto.setPetShopId(price.getPetShop().getId());
                dto.setPetShopName(price.getPetShop().getName());
                dto.setCity(price.getPetShop().getCity());
                dto.setStoreAddress(price.getPetShop().getAddress());
            }

            return dto;
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class PaymentMethodDto {
        private String code;
        private String name;
        private String termsUrl;
        private String notes;
        private Instant verifiedAt;

        public static PaymentMethodDto fromEntity(StorePaymentMethod method) {
            PaymentMethodDto dto = new PaymentMethodDto();

            dto.setCode(method.getPaymentProvider().getCode());
            dto.setName(method.getPaymentProvider().getName());
            dto.setTermsUrl(method.getTermsUrl());
            dto.setNotes(method.getNotes());
            dto.setVerifiedAt(method.getVerifiedAt());

            return dto;
        }
    }


}
