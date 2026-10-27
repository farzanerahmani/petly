package com.petly.food.entity;

import com.petly.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 10/25/2026
 */
@Entity
@Table(name = "payment_providers")
@Getter
@Setter
@NoArgsConstructor
public class PaymentProvider extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "website_url", columnDefinition = "TEXT")
    private String websiteUrl;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}

