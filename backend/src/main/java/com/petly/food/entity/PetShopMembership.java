package com.petly.food.entity;

import com.petly.common.entity.AuditableEntity;
import com.petly.food.entity.enums.PetShopMemberRole;
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
        name = "pet_shop_memberships",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_pet_shop_membership",
                        columnNames = {"user_id", "pet_shop_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_pet_shop_memberships_user_active",
                        columnList = "user_id, is_active"
                ),
                @Index(
                        name = "idx_pet_shop_memberships_shop_active",
                        columnList = "pet_shop_id, is_active"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class PetShopMembership extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pet_shop_memberships_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "pet_shop_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pet_shop_memberships_pet_shop")
    )
    private PetShop petShop;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PetShopMemberRole role;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}

