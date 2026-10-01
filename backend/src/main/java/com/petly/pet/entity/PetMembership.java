package com.petly.pet.entity;

import com.petly.common.entity.BaseEntity;
import com.petly.pet.entity.enums.PetMembershipRole;
import com.petly.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
@Entity
@Table(name = "pet_memberships",uniqueConstraints = {
        @UniqueConstraint(name = "uk_pet_memberships_user_pet",
        columnNames = {"user_id","pet_id"})
})
@Getter
@Setter
public class PetMembership extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "pet_id",nullable = false)
    private Pet pet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 30)
    private PetMembershipRole role;
}
