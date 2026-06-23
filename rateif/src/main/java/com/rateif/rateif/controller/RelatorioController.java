package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.model.Relatorio;
import com.rateif.rateif.repository.RelatorioRepository;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioRepository repository;

    public RelatorioController(RelatorioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Relatorio> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Relatorio salvar(@RequestBody Relatorio relatorio) {
        return repository.save(relatorio);
    }
}