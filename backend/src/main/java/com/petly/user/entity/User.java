package com.petly.user.entity;

import com.petly.common.entity.AuditableEntity;
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
public class User extends AuditableEntity {

    @Column(name = "phone_number", nullable = false, length = 20, unique = true)
    private String phoneNumber;


}
