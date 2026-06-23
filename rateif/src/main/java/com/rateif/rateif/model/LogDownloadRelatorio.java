package com.rateif.rateif.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class LogDownloadRelatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLog;

    @ManyToOne
    @JoinColumn(name = "id_relatorio")
    private Relatorio relatorio;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private LocalDateTime dataDownload;

    private String extensao;
}