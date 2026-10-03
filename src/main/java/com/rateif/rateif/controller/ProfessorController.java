package com.rateif.rateif.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.repository.ProfessorRepository;

@RestController
@RequestMapping("/professores")
public class ProfessorController {
    private final ProfessorRepository repository;
    public ProfessorController(ProfessorRepository repository){this.repository=repository;}
    @GetMapping
    public List<Map<String,Object>> listar(){
        return repository.findAll().stream().map(p -> Map.<String,Object>of(
                "id", p.getId(),
                "nome", p.getUsuario()==null?"Professor #"+p.getId():p.getUsuario().getNome())) .toList();
    }
}
