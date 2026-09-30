package com.petly.user.service;

import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author farzane.rahmani
 * @created 9/30/2026
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(String phoneNumber){
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new IllegalArgumentException("phone number already exists");
        }

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }
}
