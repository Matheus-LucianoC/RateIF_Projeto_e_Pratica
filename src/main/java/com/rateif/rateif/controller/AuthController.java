package com.rateif.rateif.controller;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rateif.rateif.dto.CadastroRequest;
import com.rateif.rateif.dto.GoogleLoginRequest;
import com.rateif.rateif.dto.LoginRequest;
import com.rateif.rateif.dto.RecuperacaoRequest;
import com.rateif.rateif.model.RecuperacaoSenha;
import com.rateif.rateif.model.Usuario;
import com.rateif.rateif.repository.RecuperacaoSenhaRepository;
import com.rateif.rateif.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RecuperacaoSenhaRepository recuperacaoRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthController(UsuarioRepository usuarioRepository,
                          RecuperacaoSenhaRepository recuperacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.recuperacaoRepository = recuperacaoRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        if (request == null || request.email() == null || request.senha() == null) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Preencha e-mail e senha."));
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.email().trim())
                .orElse(null);

        if (usuario == null || !Boolean.TRUE.equals(usuario.getStatus())
                || !passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "E-mail ou senha inválidos."));
        }

        iniciarSessao(session, usuario);
        return ResponseEntity.ok(Map.of(
                "mensagem", "Login realizado com sucesso.",
                "redirect", "/dashboard"
        ));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody CadastroRequest request, HttpSession session) {
        if (request == null || vazio(request.nome()) || vazio(request.email())
                || vazio(request.senha()) || vazio(request.confirmarSenha())) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Preencha todos os campos."));
        }

        if (request.nome().trim().length() < 3) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Informe um nome válido."));
        }

        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (!email.contains("@") || !email.contains(".")) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Informe um e-mail válido."));
        }

        if (request.senha().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "A senha deve ter pelo menos 6 caracteres."));
        }

        if (!request.senha().equals(request.confirmarSenha())) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "As senhas não coincidem."));
        }

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", "Já existe um usuário com esse e-mail."));
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPerfil("USUARIO");
        usuario.setStatus(true);
        usuario.setCriadoEm(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);
        iniciarSessao(session, usuario);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Cadastro realizado com sucesso.",
                "redirect", "/dashboard"
        ));
    }

    @PostMapping("/recuperacao")
    public ResponseEntity<?> recuperacao(@RequestBody RecuperacaoRequest request) {
        if (request == null || vazio(request.email())) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Informe o e-mail."));
        }

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);

        // Não revela ao cliente se o e-mail existe ou não.
        if (usuario == null) {
            return ResponseEntity.ok(Map.of(
                    "mensagem", "Se o e-mail estiver cadastrado, um link de recuperação será disponibilizado."
            ));
        }

        RecuperacaoSenha rec = new RecuperacaoSenha();
        rec.setUsuario(usuario);
        rec.setToken(UUID.randomUUID().toString());
        rec.setExpiraEm(LocalDateTime.now().plusMinutes(30));
        rec.setUsadoEm(null);
        recuperacaoRepository.save(rec);

        // Nesta versão acadêmica, o link é retornado para permitir testes locais.
        String link = "/recuperacao/nova?token=" + rec.getToken();

        return ResponseEntity.ok(Map.of(
                "mensagem", "Link de recuperação gerado para teste.",
                "link", link
        ));
    }

    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody GoogleLoginRequest request, HttpSession session) {
        if (request == null || vazio(request.credential())) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Credencial do Google não informada."));
        }

        try {
            String json = restClient.get()
                    .uri("https://oauth2.googleapis.com/tokeninfo?id_token={token}", request.credential())
                    .retrieve()
                    .body(String.class);

            JsonNode token = objectMapper.readTree(json);

            String email = token.path("email").asText("");
            String name = token.path("name").asText("Usuário Google");
            String emailVerified = token.path("email_verified").asText("");

            if (email.isBlank() || !"true".equalsIgnoreCase(emailVerified)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensagem", "A conta Google não pôde ser validada."));
            }

            Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email).orElse(null);

            if (usuario == null) {
                usuario = new Usuario();
                usuario.setNome(name);
                usuario.setEmail(email.toLowerCase(Locale.ROOT));
                // Usuário Google não usa a senha local como credencial.
                usuario.setSenhaHash(passwordEncoder.encode(UUID.randomUUID().toString()));
                usuario.setPerfil("USUARIO");
                usuario.setStatus(true);
                usuario.setCriadoEm(LocalDateTime.now());
                usuario = usuarioRepository.save(usuario);
            } else {
                if (!Boolean.TRUE.equals(usuario.getStatus())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("mensagem", "Usuário desativado."));
                }

                if (!name.isBlank() && !name.equals(usuario.getNome())) {
                    usuario.setNome(name);
                    usuarioRepository.save(usuario);
                }
            }

            iniciarSessao(session, usuario);

            return ResponseEntity.ok(Map.of(
                    "mensagem", "Login com Google realizado com sucesso.",
                    "redirect", "/dashboard"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Não foi possível validar a conta Google. Configure o Client ID e tente novamente."));
        }
    }

    @PostMapping("/recuperacao/nova")
    public ResponseEntity<?> novaSenha(@RequestParam String token,
                                       @RequestBody Map<String, String> body) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "Token de recuperação ausente."));
        }

        String senha = body == null ? null : body.get("senha");
        String confirmar = body == null ? null : body.get("confirmarSenha");

        if (senha == null || confirmar == null || senha.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "A senha deve ter pelo menos 6 caracteres."));
        }

        if (!senha.equals(confirmar)) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", "As senhas não coincidem."));
        }

        RecuperacaoSenha rec = recuperacaoRepository.findByToken(token).orElse(null);

        if (rec == null || rec.getUsadoEm() != null
                || rec.getExpiraEm() == null || rec.getExpiraEm().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensagem", "Link de recuperação inválido ou expirado."));
        }

        Usuario usuario = rec.getUsuario();
        usuario.setSenhaHash(passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);

        rec.setUsadoEm(LocalDateTime.now());
        recuperacaoRepository.save(rec);

        return ResponseEntity.ok(Map.of(
                "mensagem", "Senha alterada com sucesso.",
                "redirect", "/login"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("redirect", "/login"));
    }

    private void iniciarSessao(HttpSession session, Usuario usuario) {
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNome", usuario.getNome());
        session.setAttribute("usuarioEmail", usuario.getEmail());
        session.setAttribute("usuarioPerfil", usuario.getPerfil());
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
