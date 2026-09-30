package com.petly.user.service;

import com.petly.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;


/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */

public class UserServiceUTest extends BaseUserService {

    @Test
    void shouldCreateUserWithNewPhoneNumber() {

        Mockito.when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(createMockedUser());

        var response = userService.createUser(createMockedCreateUserRequest());

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getPhoneNumber(), phoneNumber);
        Mockito.verify(userRepository).existsByPhoneNumber(phoneNumber);
        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldNotCreateUserWithExistsPhoneNumber() {
        Mockito.when(userRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);
        Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> userService.createUser(createMockedCreateUserRequest()));

        Mockito.verify(userRepository).existsByPhoneNumber(phoneNumber);
        Mockito.verify(userRepository, Mockito.never()).save(any(User.class));
    }
}
