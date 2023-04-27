package com.example.shop.config.security;

import org.springframework.security.core.AuthenticationException;

public class JwtUserNotFoundException extends AuthenticationException {
    public JwtUserNotFoundException(String message) {
        super(message);
    }

    public JwtUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
