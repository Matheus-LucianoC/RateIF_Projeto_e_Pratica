package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.model.Tecnico;
import com.rateif.rateif.repository.TecnicoRepository;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoRepository repository;

    public TecnicoController(TecnicoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tecnico> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Tecnico salvar(@RequestBody Tecnico tecnico) {
        return repository.save(tecnico);
    }
}