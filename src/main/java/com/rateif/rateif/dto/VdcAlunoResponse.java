package com.rateif.rateif.dto;

public record VdcAlunoResponse(
        Integer id,
        String nome,
        String matricula,
        long aprovar,
        long recuperacao,
        long reter,
        long abstencao,
        String meuVoto,
        String resultado) {}
