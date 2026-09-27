package com.petly.common.exception;

import java.util.Map;

/**
 * @author farzane.rahmani
 * @created 9/27/2026
 */
public record ApiError(String code,
                       String message,
                       Map<String,String> details) {
}
