package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.model.RecuperacaoSenha;
import com.rateif.rateif.repository.RecuperacaoSenhaRepository;

@RestController
@RequestMapping("/recuperacao")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaRepository repository;

    public RecuperacaoSenhaController(RecuperacaoSenhaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<RecuperacaoSenha> listar() {
        return repository.findAll();
    }

    @PostMapping
    public RecuperacaoSenha salvar(@RequestBody RecuperacaoSenha rec) {
        return repository.save(rec);
    }
}