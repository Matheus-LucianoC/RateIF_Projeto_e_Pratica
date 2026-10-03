package com.rateif.rateif;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.rateif.rateif.model.Usuario;
import com.rateif.rateif.service.MobileTokenService;

class MobileTokenServiceTest {

    @Test
    void deveGerarEValidarToken() {
        MobileTokenService service = new MobileTokenService(
                "12345678901234567890123456789012",
                1
        );

        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@rateif.local");
        usuario.setPerfil("USUARIO");
        usuario.setStatus(true);

        String token = service.generateToken(usuario);
        MobileTokenService.MobileTokenPrincipal principal = service.validateBearer("Bearer " + token);

        assertEquals(10, principal.userId());
        assertEquals("teste@rateif.local", principal.email());
    }

    @Test
    void deveRejeitarTokenAlterado() {
        MobileTokenService service = new MobileTokenService(
                "12345678901234567890123456789012",
                1
        );

        Usuario usuario = new Usuario();
        usuario.setId(10);
        usuario.setNome("Usuário Teste");
        usuario.setEmail("teste@rateif.local");
        usuario.setPerfil("USUARIO");
        usuario.setStatus(true);

        String token = service.generateToken(usuario);
        String adulterado = token.substring(0, token.length() - 1) + (token.endsWith("A") ? "B" : "A");

        assertThrows(
                MobileTokenService.InvalidMobileTokenException.class,
                () -> service.validateBearer("Bearer " + adulterado)
        );
    }
}
