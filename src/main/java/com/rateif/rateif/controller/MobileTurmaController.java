package com.rateif.rateif.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rateif.rateif.dto.TurmaRequest;
import com.rateif.rateif.service.MobileTokenService;
import com.rateif.rateif.service.TurmaService;

@RestController
@RequestMapping("/api/mobile/turmas")
@CrossOrigin(origins = "*")
public class MobileTurmaController {
    private final MobileTokenService tokenService;
    private final TurmaService service;
    public MobileTurmaController(MobileTokenService tokenService, TurmaService service) { this.tokenService = tokenService; this.service = service; }

    @GetMapping public ResponseEntity<?> listar(@RequestHeader(value="Authorization", required=false) String auth) {
        if (!ativo(auth)) return unauthorized(); return ResponseEntity.ok(service.listar());
    }
    @PostMapping public ResponseEntity<?> criar(@RequestHeader(value="Authorization", required=false) String auth, @RequestBody TurmaRequest r) {
        if (!ativo(auth)) return unauthorized(); try { return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(null, r)); } catch (IllegalArgumentException e) { return bad(e); }
    }
    @PutMapping("/{id}") public ResponseEntity<?> atualizar(@RequestHeader(value="Authorization", required=false) String auth, @PathVariable Integer id, @RequestBody TurmaRequest r) {
        if (!ativo(auth)) return unauthorized(); try { return ResponseEntity.ok(service.salvar(id, r)); } catch (IllegalArgumentException e) { return bad(e); }
    }
    @DeleteMapping("/{id}") public ResponseEntity<?> excluir(@RequestHeader(value="Authorization", required=false) String auth, @PathVariable Integer id) {
        if (!ativo(auth)) return unauthorized(); try { service.excluir(id); return ResponseEntity.noContent().build(); } catch (IllegalArgumentException e) { return ResponseEntity.status(404).body(Map.of("mensagem", e.getMessage())); } catch (IllegalStateException e) { return ResponseEntity.status(409).body(Map.of("mensagem", e.getMessage())); }
    }
    private boolean ativo(String auth) { try { tokenService.validateBearer(auth); return true; } catch (Exception e) { return false; } }
    private ResponseEntity<?> unauthorized() { return ResponseEntity.status(401).body(Map.of("mensagem", "Sessão mobile inválida ou expirada.")); }
    private ResponseEntity<?> bad(Exception e) { return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage())); }
}
