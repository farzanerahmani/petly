package com.petly.user.controller;


import com.petly.user.controller.dto.CreateUserRequestDto;
import com.petly.user.controller.dto.CreateUserResponseDto;
import com.petly.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

/**
 * @author farzane.rahmani
 * @created 10/1/2026
 */
@WebMvcTest(UserController.class)
public abstract class BaseUserController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
    @MockitoBean
    protected UserService userService;

    protected String phoneNumber = "09127808205";

    protected CreateUserRequestDto createMockedUserRequestDto() {
        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.setPhoneNumber(phoneNumber);
        return requestDto;
    }

    protected CreateUserResponseDto createMockedUserResponseDto() {
        CreateUserResponseDto responseDto = new CreateUserResponseDto();
        responseDto.setId(1L);
        responseDto.setPhoneNumber(phoneNumber);
        return responseDto;
    }

}
