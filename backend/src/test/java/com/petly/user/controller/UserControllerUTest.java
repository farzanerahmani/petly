package com.petly.user.controller;

import com.petly.user.controller.dto.CreateUserRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
public class UserControllerUTest extends BaseUserController{

    @Test
    void shouldIdCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(CreateUserRequestDto.class)))
                .thenReturn(createMockedUserResponseDto());

        mockMvc.perform(
                        post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createMockedUserRequestDto()))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.phoneNumber")
                        .value(phoneNumber));
    }
}
