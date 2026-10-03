package com.rateif.rateif.dto;

public record TurmaResponse(Integer id, String nomeTurma, Integer anoLetivo, String turno, Integer professorId, String professorNome, long alunos) {}
