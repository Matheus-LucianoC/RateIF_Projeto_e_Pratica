package com.rateif.rateif.dto;

import java.time.LocalDateTime;
import java.util.List;

public record VdcResponse(
        Integer id,
        String titulo,
        Integer turmaId,
        String turmaNome,
        Integer anoLetivo,
        String status,
        LocalDateTime criadoEm,
        LocalDateTime encerradoEm,
        List<VdcAlunoResponse> alunos) {}
