package com.rateif.rateif.dto;

import com.rateif.rateif.model.Usuario;

public record MobileUserResponse(
        Integer id,
        String nome,
        String email,
        String perfil,
        Boolean status
) {
    public static MobileUserResponse from(Usuario usuario) {
        return new MobileUserResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getStatus()
        );
    }
}
