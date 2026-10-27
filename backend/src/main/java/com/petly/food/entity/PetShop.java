
package com.petly.food.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "pet_shops",
        indexes = {
                @Index(
                        name = "idx_pet_shops_city_active",
                        columnList = "city, is_active"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class PetShop extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 100)
    private String city = "Tehran";

    @Column(name = "website_url", columnDefinition = "TEXT")
    private String websiteUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "supports_delivery", nullable = false)
    private boolean supportsDelivery = false;

    @Column(name = "delivery_notes", columnDefinition = "TEXT")
    private String deliveryNotes;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
