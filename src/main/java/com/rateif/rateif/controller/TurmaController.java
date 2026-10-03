package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rateif.rateif.dto.TurmaRequest;
import com.rateif.rateif.dto.TurmaResponse;
import com.rateif.rateif.model.Turma;
import com.rateif.rateif.repository.TurmaRepository;
import com.rateif.rateif.service.TurmaService;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {
    private final TurmaRepository repository;
    private final TurmaService service;

    public TurmaController(TurmaRepository repository, TurmaService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public List<TurmaResponse> listar() { return service.listar(); }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody TurmaRequest request) {
        return responder(() -> service.salvar(null, request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody TurmaRequest request) {
        return responder(() -> service.salvar(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("mensagem", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(java.util.Map.of("mensagem", e.getMessage()));
        }
    }

    private ResponseEntity<?> responder(java.util.function.Supplier<TurmaResponse> action, HttpStatus ok) {
        try { return ResponseEntity.status(ok).body(action.get()); }
        catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body(java.util.Map.of("mensagem", e.getMessage())); }
    }
}
