package com.petly.user.service;

import com.petly.user.controller.dto.CreateUserRequestDto;
import com.petly.user.controller.dto.CreateUserResponseDto;
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
    public CreateUserResponseDto createUser(CreateUserRequestDto requestDto){
        if(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())){
            throw new IllegalArgumentException("phone number already exists");
        }

        User user = new User();
        user.setPhoneNumber(requestDto.getPhoneNumber());
        var response = userRepository.save(user);
        CreateUserResponseDto responseDto = new CreateUserResponseDto();
        responseDto.setId(response.getId());
        responseDto.setPhoneNumber(response.getPhoneNumber());
        return responseDto;
    }
}
