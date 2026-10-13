package com.petly.common.exception;

/**
 * @author farzane.rahmani
 * @created 10/11/2026
 */
public class InvalidOtpException extends RuntimeException {

    public InvalidOtpException() {
        super("Invalid or expired OTP");
    }
}
