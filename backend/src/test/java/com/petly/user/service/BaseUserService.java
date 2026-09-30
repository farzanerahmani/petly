package com.petly.user.service;

import com.petly.user.controller.dto.CreateUserRequestDto;
import com.petly.user.entity.User;
import com.petly.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseUserService {
    protected String phoneNumber = "09127808205";
    @Mock
    protected UserRepository userRepository;

    @InjectMocks
    protected UserService userService;

    @BeforeEach
    void setup(){
        userService = new UserService(userRepository);
    }

    protected User createMockedUser(){
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        return user;
    }

    protected CreateUserRequestDto createMockedCreateUserRequest(){
        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.setPhoneNumber(phoneNumber);
        return requestDto;
    }
}
