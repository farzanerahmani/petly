package com.petly.user.controller;

import com.petly.common.response.ApiResponse;
import com.petly.user.controller.dto.CreateUserRequestDto;
import com.petly.user.controller.dto.CreateUserResponseDto;
import com.petly.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateUserResponseDto>> createUser(@Valid @RequestBody CreateUserRequestDto requestDto){
        CreateUserResponseDto responseDto =
                userService.createUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto));
    }
}
