package com.petly.common.response;

/**
 * @author farzane.rahmani
 * @created 9/27/2026
 */
public record ApiResponse<T> (T data){

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(data);
    }
}
