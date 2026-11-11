
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
                ),
                @Index(
                        name = "idx_food_prices_listing_checked",
                        columnList = "food_listing_id, checked_at DESC"
                ),
                @Index(
                        name = "idx_food_prices_listing_check",
                        columnList = "listing_check_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FoodPrice extends BaseEntity {

    /*
     * Canonical product is optional.
     * A price can be stored before product matching is approved.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "food_product_id", nullable = true)
    private FoodProduct foodProduct;

    /*
     * The original shop listing that produced this price.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "food_listing_id")
    private FoodListing foodListing;

    @Column(name = "listing_check_id")
    private Long listingCheckId;

    /*
     * The scraper observation associated with this price.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumns({
            @JoinColumn(
                    name = "listing_check_id",
                    referencedColumnName = "id",
                    insertable = false,
                    updatable = false
            ),
            @JoinColumn(
                    name = "food_listing_id",
                    referencedColumnName = "food_listing_id",
                    insertable = false,
                    updatable = false
            )
    })
    private FoodListingCheck listingCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_shop_id")
    private PetShop petShop;

    @Column(name = "seller_name", nullable = false, length = 200)
    private String sellerName;

    /*
     * Null means the scraper did not provide a valid price.
     * Zero is never used to represent an unknown price.
     */
    @Column(name = "price_amount", precision = 14, scale = 2)
    private BigDecimal priceAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private FoodCurrency currency;

    /*
     * Legacy field retained for backward compatibility.
     * New package weight data belongs to FoodVariant.
     */
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
