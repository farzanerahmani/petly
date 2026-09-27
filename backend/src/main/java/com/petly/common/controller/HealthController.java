package com.petly.common.controller;

import com.petly.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author farzane.rahmani
 * @created 9/25/2026
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/healthCheck")
    public ApiResponse<Map<String,String>> healthCheck(){

        return  ApiResponse.success(Map.of("status","UP"));
    }
}
