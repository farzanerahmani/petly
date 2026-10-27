package com.petly.food.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * @author farzane.rahmani
 * @created 10/25/2026
 */
@Entity
@Table(
        name = "store_payment_methods",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_store_payment_provider",
                        columnNames = {"pet_shop_id", "payment_provider_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_store_payment_methods_active",
                        columnList = "pet_shop_id, is_active"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class StorePaymentMethod extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_shop_id", nullable = false)
    private PetShop petShop;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_provider_id", nullable = false)
    private PaymentProvider paymentProvider;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "terms_url", columnDefinition = "TEXT")
    private String termsUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "verified_at")
    private Instant verifiedAt;
}

