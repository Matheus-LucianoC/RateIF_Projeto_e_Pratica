package com.rateif.rateif.controller;

import java.util.Locale;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.dto.MobilePerfilRequest;
import com.rateif.rateif.dto.MobileUserResponse;
import com.rateif.rateif.model.Usuario;
import com.rateif.rateif.repository.UsuarioRepository;
import com.rateif.rateif.service.MobileTokenService;

@RestController
@RequestMapping("/api/mobile/profile")
@CrossOrigin(origins = "*")
public class MobileProfileController {

    private final UsuarioRepository usuarioRepository;
    private final MobileTokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public MobileProfileController(
            UsuarioRepository usuarioRepository,
            MobileTokenService tokenService,
            BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> perfil(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Usuario usuario = usuarioAutenticado(authorization);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Sessão mobile inválida ou expirada."));
        }
        return ResponseEntity.ok(MobileUserResponse.from(usuario));
    }

    @PutMapping
    public ResponseEntity<?> atualizar(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody MobilePerfilRequest request) {
        Usuario usuario = usuarioAutenticado(authorization);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Sessão mobile inválida ou expirada."));
        }

        if (request == null || vazio(request.nome()) || vazio(request.email())) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Nome e e-mail são obrigatórios."));
        }
        if (request.nome().trim().length() < 3) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Informe um nome válido."));
        }

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (!email.contains("@") || !email.contains(".")) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Informe um e-mail válido."));
        }

        Usuario outro = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);
        if (outro != null && !outro.getId().equals(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", "Esse e-mail já está sendo usado por outro usuário."));
        }

        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuarioRepository.save(usuario);

        // Gera um novo token para refletir o e-mail/nome atualizados no próximo request.
        String novoToken = tokenService.generateToken(usuario);
        return ResponseEntity.ok(Map.of(
                "mensagem", "Perfil atualizado com sucesso.",
                "token", novoToken,
                "expiresIn", tokenService.getExpirationSeconds(),
                "usuario", MobileUserResponse.from(usuario)
        ));
    }

    private Usuario usuarioAutenticado(String authorization) {
        try {
            MobileTokenService.MobileTokenPrincipal principal = tokenService.validateBearer(authorization);
            return usuarioRepository.findById(principal.userId())
                    .filter(usuario -> Boolean.TRUE.equals(usuario.getStatus()))
                    .orElse(null);
        } catch (MobileTokenService.InvalidMobileTokenException e) {
            return null;
        }
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
