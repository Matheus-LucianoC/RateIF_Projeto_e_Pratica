package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.model.Professor;
import com.rateif.rateif.repository.ProfessorRepository;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorRepository repository;

    public ProfessorController(ProfessorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Professor> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Professor salvar(@RequestBody Professor professor) {
        return repository.save(professor);
    }
}