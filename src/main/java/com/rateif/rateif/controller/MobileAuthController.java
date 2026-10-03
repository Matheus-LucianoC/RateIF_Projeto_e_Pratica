package com.rateif.rateif.controller;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.dto.CadastroRequest;
import com.rateif.rateif.dto.LoginRequest;
import com.rateif.rateif.dto.MobileAuthResponse;
import com.rateif.rateif.dto.MobileUserResponse;
import com.rateif.rateif.model.Usuario;
import com.rateif.rateif.repository.UsuarioRepository;
import com.rateif.rateif.service.MobileTokenService;

@RestController
@RequestMapping("/api/mobile/auth")
@CrossOrigin(origins = "*")
public class MobileAuthController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MobileTokenService tokenService;

    public MobileAuthController(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder,
            MobileTokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null || vazio(request.email()) || vazio(request.senha())) {
            return erro(HttpStatus.BAD_REQUEST, "Preencha e-mail e senha.");
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.email().trim()).orElse(null);
        if (usuario == null || !Boolean.TRUE.equals(usuario.getStatus())
                || !passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            return erro(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        }

        return ResponseEntity.ok(respostaAutenticada(usuario, "Login realizado com sucesso."));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody CadastroRequest request) {
        if (request == null || vazio(request.nome()) || vazio(request.email())
                || vazio(request.senha()) || vazio(request.confirmarSenha())) {
            return erro(HttpStatus.BAD_REQUEST, "Preencha todos os campos.");
        }

        if (request.nome().trim().length() < 3) {
            return erro(HttpStatus.BAD_REQUEST, "Informe um nome válido.");
        }
        if (request.senha().length() < 6) {
            return erro(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 6 caracteres.");
        }
        if (!request.senha().equals(request.confirmarSenha())) {
            return erro(HttpStatus.BAD_REQUEST, "As senhas não coincidem.");
        }

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (!email.contains("@") || !email.contains(".")) {
            return erro(HttpStatus.BAD_REQUEST, "Informe um e-mail válido.");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            return erro(HttpStatus.CONFLICT, "Já existe um usuário com esse e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPerfil("USUARIO");
        usuario.setStatus(true);
        usuario.setCriadoEm(LocalDateTime.now());

        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respostaAutenticada(usuario, "Cadastro realizado com sucesso."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization != null && !authorization.isBlank()) {
            try {
                tokenService.validateBearer(authorization);
            } catch (MobileTokenService.InvalidMobileTokenException ignored) {
                // Logout continua sendo idempotente para o aplicativo.
            }
        }
        return ResponseEntity.ok(Map.of("mensagem", "Sessão mobile encerrada."));
    }

    private MobileAuthResponse respostaAutenticada(Usuario usuario, String mensagem) {
        String token = tokenService.generateToken(usuario);
        return new MobileAuthResponse(
                mensagem,
                token,
                tokenService.getExpirationSeconds(),
                MobileUserResponse.from(usuario)
        );
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("mensagem", mensagem));
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
