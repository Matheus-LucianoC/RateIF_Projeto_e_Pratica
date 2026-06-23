package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.model.Turma;
import com.rateif.rateif.repository.TurmaRepository;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    private final TurmaRepository repository;

    public TurmaController(TurmaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Turma> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Turma salvar(@RequestBody Turma turma) {
        return repository.save(turma);
    }
}