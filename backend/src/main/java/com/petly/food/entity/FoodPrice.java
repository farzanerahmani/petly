
package com.petly.food.entity;

import com.petly.common.entity.BaseEntity;
import com.petly.food.entity.enums.FoodCurrency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "food_prices",
        indexes = {
                @Index(
                        name = "idx_food_prices_product_checked",
                        columnList = "food_product_id, checked_at DESC"
                ),
                @Index(
                        name = "idx_food_prices_shop_checked",
                        columnList = "pet_shop_id, checked_at DESC"
                ),
                @Index(
                        name = "idx_food_prices_available",
                        columnList = "food_product_id, is_available, checked_at DESC"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FoodPrice extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_product_id", nullable = false)
    private FoodProduct foodProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_shop_id")
    private PetShop petShop;

    @Column(name = "seller_name", nullable = false, length = 200)
    private String sellerName;

    @Column(name = "price_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal priceAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private FoodCurrency currency;

    @Column(name = "package_weight_grams")
    private Integer packageWeightGrams;

    @Column(name = "product_url", columnDefinition = "TEXT")
    private String productUrl;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "stock_checked_at")
    private Instant stockCheckedAt;

    @Column(name = "last_verified_at")
    private Instant lastVerifiedAt;

    @Column(name = "checked_at", nullable = false)
    private Instant checkedAt = Instant.now();
}
