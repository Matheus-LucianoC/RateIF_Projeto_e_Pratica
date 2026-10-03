package com.rateif.rateif.dto;

public record MobileAuthResponse(
        String mensagem,
        String token,
        long expiresIn,
        MobileUserResponse usuario
) {}
