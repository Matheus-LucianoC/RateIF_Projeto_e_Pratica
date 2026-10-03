package com.rateif.rateif.service;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rateif.rateif.model.Usuario;

@Service
public class MobileTokenService {

    private static final String ALGORITHM = "HmacSHA256";
    private static final String HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecretKeySpec secretKey;
    private final long expirationSeconds;

    public MobileTokenService(
            @Value("${mobile.jwt.secret}") String secret,
            @Value("${mobile.jwt.expiration-hours:8}") long expirationHours) {

        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException(
                    "mobile.jwt.secret precisa ter pelo menos 32 caracteres.");
        }

        if (expirationHours <= 0) {
            throw new IllegalArgumentException(
                    "mobile.jwt.expiration-hours precisa ser maior que zero.");
        }

        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                ALGORITHM
        );

        this.expirationSeconds = expirationHours * 3600L;
    }

    public String generateToken(Usuario usuario) {

        long now = Instant.now().getEpochSecond();
        long expiresAt = now + expirationSeconds;

        Map<String, Object> claims = new HashMap<>();

        claims.put("sub", String.valueOf(usuario.getId()));
        claims.put("email", usuario.getEmail());
        claims.put("name", usuario.getNome());
        claims.put("profile", usuario.getPerfil());
        claims.put("iat", now);
        claims.put("exp", expiresAt);

        try {
            String encodedHeader =
                    base64Url(HEADER.getBytes(StandardCharsets.UTF_8));

            String encodedPayload =
                    base64Url(objectMapper.writeValueAsBytes(claims));

            String content = encodedHeader + "." + encodedPayload;

            return content + "." + sign(content);

        } catch (JsonProcessingException | GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Não foi possível gerar o token mobile.", e);
        }
    }

    public MobileTokenPrincipal validateBearer(String authorizationHeader) {

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            throw new InvalidMobileTokenException(
                    "Token de acesso ausente.");
        }

        String token = authorizationHeader
                .substring("Bearer ".length())
                .trim();

        if (token.isBlank()) {
            throw new InvalidMobileTokenException(
                    "Token de acesso ausente.");
        }

        String[] pieces = token.split("\\.", -1);

        if (pieces.length != 3) {
            throw new InvalidMobileTokenException(
                    "Token de acesso inválido.");
        }

        try {

            String content = pieces[0] + "." + pieces[1];

            String expectedSignature = sign(content);

            if (!MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.US_ASCII),
                    pieces[2].getBytes(StandardCharsets.US_ASCII))) {

                throw new InvalidMobileTokenException(
                        "Token de acesso inválido.");
            }

            JsonNode header = objectMapper.readTree(
                    decodeBase64Url(pieces[0]));

            if (!"HS256".equals(header.path("alg").asText())
                    || !"JWT".equals(header.path("typ").asText())) {

                throw new InvalidMobileTokenException(
                        "Tipo de token não suportado.");
            }

            JsonNode payload = objectMapper.readTree(
                    decodeBase64Url(pieces[1]));

            long exp = payload.path("exp").asLong(0);
            long now = Instant.now().getEpochSecond();

            if (exp == 0 || exp <= now) {
                throw new InvalidMobileTokenException(
                        "Token de acesso expirado.");
            }

            int userId = Integer.parseInt(
                    payload.path("sub").asText());

            String email = payload.path("email").asText("");

            return new MobileTokenPrincipal(userId, email);

        } catch (InvalidMobileTokenException e) {

            throw e;

        } catch (Exception e) {

            throw new InvalidMobileTokenException(
                    "Token de acesso inválido.");
        }
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    private String sign(String content)
            throws GeneralSecurityException {

        Mac mac = Mac.getInstance(ALGORITHM);

        mac.init(secretKey);

        return base64Url(
                mac.doFinal(
                        content.getBytes(StandardCharsets.UTF_8)
                )
        );
    }

    private String base64Url(byte[] data) {

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(data);
    }

    private byte[] decodeBase64Url(String value) {

        return Base64.getUrlDecoder().decode(value);
    }

    public record MobileTokenPrincipal(
            int userId,
            String email) {
    }

    public static class InvalidMobileTokenException
            extends RuntimeException {

        public InvalidMobileTokenException(String message) {
            super(message);
        }
    }
}