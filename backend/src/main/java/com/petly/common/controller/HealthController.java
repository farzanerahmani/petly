package com.petly.common.controller;

import com.petly.common.exception.ApiError;
import com.petly.common.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author farzane.rahmani
 * @created 9/25/2026
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/healthCheck")
    public ApiResponse<Map<String, String>> healthCheck() {

        return ApiResponse.success(Map.of("status", "UP"));
    }


    public record TestRequest(@NotBlank(message = "name is required")
                              String name) {
    }

    @PostMapping("/test-validation")
    public ApiResponse<String> testValidation(@Valid @RequestBody TestRequest request){
        return ApiResponse.success(request.name());
    }

}
