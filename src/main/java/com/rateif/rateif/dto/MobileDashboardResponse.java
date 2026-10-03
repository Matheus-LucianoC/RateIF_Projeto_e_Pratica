package com.rateif.rateif.dto;

public record MobileDashboardResponse(
        long alunos,
        long turmas,
        long avaliacoes,
        long relatorios,
        long votacoesAbertas) {}
