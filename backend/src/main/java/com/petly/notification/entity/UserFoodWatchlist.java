package com.petly.notification.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 10/31/2026
 */
@Entity
@Table(
        name = "user_food_watchlist",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_user_food_watchlist",
                        columnNames = {"user_id", "food_product_id"}
                )
        }
)
@Getter
@Setter
public class UserFoodWatchlist extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "food_product_id", nullable = false)
    private Long foodProductId;
}

