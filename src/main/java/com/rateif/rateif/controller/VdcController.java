package com.rateif.rateif.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rateif.rateif.dto.*;
import com.rateif.rateif.service.VdcService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/vdc")
public class VdcController {
    private final VdcService service;
    public VdcController(VdcService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<?> listar(HttpSession session) {
        Integer uid = userId(session);
        if (uid == null) return unauthorized();
        return ResponseEntity.ok(service.listar(uid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Integer id, HttpSession session) {
        Integer uid = userId(session); if (uid == null) return unauthorized();
        try { return ResponseEntity.ok(service.detalhar(id, uid)); } catch (IllegalArgumentException e) { return bad(e); }
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody VotacaoRequest request, HttpSession session) {
        Integer uid = userId(session); if (uid == null) return unauthorized();
        try { return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request, uid)); } catch (IllegalStateException e) { return conflict(e); } catch (IllegalArgumentException e) { return bad(e); }
    }

    @PostMapping("/{id}/votos")
    public ResponseEntity<?> votar(@PathVariable Integer id, @RequestBody VotoRequest request, HttpSession session) {
        Integer uid = userId(session); if (uid == null) return unauthorized();
        try { return ResponseEntity.ok(service.votar(id, request, uid)); } catch (IllegalStateException e) { return conflict(e); } catch (IllegalArgumentException e) { return bad(e); }
    }

    @PostMapping("/{id}/encerrar")
    public ResponseEntity<?> encerrar(@PathVariable Integer id, HttpSession session) {
        Integer uid = userId(session); if (uid == null) return unauthorized();
        try { return ResponseEntity.ok(service.encerrar(id, uid)); } catch (IllegalArgumentException e) { return bad(e); }
    }

    private Integer userId(HttpSession session) { Object id = session.getAttribute("usuarioId"); return id instanceof Integer ? (Integer) id : id == null ? null : Integer.valueOf(id.toString()); }
    private ResponseEntity<?> unauthorized() { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem", "Sessão inválida.")); }
    private ResponseEntity<?> bad(Exception e) { return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage())); }
    private ResponseEntity<?> conflict(Exception e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensagem", e.getMessage())); }
}
