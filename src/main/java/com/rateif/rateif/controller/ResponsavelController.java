package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.model.Responsavel;
import com.rateif.rateif.repository.ResponsavelRepository;

@RestController
@RequestMapping("/responsaveis")
public class ResponsavelController {

    private final ResponsavelRepository repository;

    public ResponsavelController(ResponsavelRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Responsavel> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Responsavel salvar(@RequestBody Responsavel responsavel) {
        return repository.save(responsavel);
    }
}