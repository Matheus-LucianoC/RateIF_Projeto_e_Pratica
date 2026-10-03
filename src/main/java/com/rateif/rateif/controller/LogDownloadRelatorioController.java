package com.rateif.rateif.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rateif.rateif.model.LogDownloadRelatorio;
import com.rateif.rateif.repository.LogDownloadRelatorioRepository;

@RestController
@RequestMapping("/logs")
public class LogDownloadRelatorioController {

    private final LogDownloadRelatorioRepository repository;

    public LogDownloadRelatorioController(LogDownloadRelatorioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<LogDownloadRelatorio> listar() {
        return repository.findAll();
    }

    @PostMapping
    public LogDownloadRelatorio salvar(@RequestBody LogDownloadRelatorio log) {
        return repository.save(log);
    }
}