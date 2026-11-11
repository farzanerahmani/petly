package com.petly.food.entity;

import com.petly.common.entity.AuditableEntity;
import com.petly.food.entity.enums.StoreOwnerRequestStatus;
import com.petly.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 11/5/2026
 */
@Entity
@Table(
        name = "store_owner_requests",
        indexes = {
                @Index(
                        name = "idx_store_owner_requests_status_created",
                        columnList = "status, created_at"
                ),
                @Index(
                        name = "idx_store_owner_requests_user_created",
                        columnList = "user_id, created_at"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class StoreOwnerRequest extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_owner_requests_user")
    )
    private User user;

    @Column(name = "shop_name", nullable = false, length = 200)
    private String shopName;

    @Column(nullable = false, length = 100)
    private String city = "Tehran";

    @Column(name = "website_url", columnDefinition = "TEXT")
    private String websiteUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoreOwnerRequestStatus status = StoreOwnerRequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reviewed_by_user_id",
            foreignKey = @ForeignKey(name = "fk_store_owner_requests_reviewer")
    )
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private java.time.Instant reviewedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "pet_shop_id",
            foreignKey = @ForeignKey(name = "fk_store_owner_requests_pet_shop")
    )
    private PetShop petShop;
}

