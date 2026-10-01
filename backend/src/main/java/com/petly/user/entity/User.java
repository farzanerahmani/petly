package com.petly.user.entity;

import com.petly.common.entity.AuditEntity;
import com.petly.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 9/30/2026
 */
@Entity
@Table(name = "users")
@Setter
@Getter
@RequiredArgsConstructor
public class User extends AuditEntity {

    @Column(name = "phone_number", nullable = false, length = 20, unique = true)
    private String phoneNumber;


}
