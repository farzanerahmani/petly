package com.petly.user.repository;


import com.petly.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author farzane.rahmani
 * @created 9/30/2026
 */
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
