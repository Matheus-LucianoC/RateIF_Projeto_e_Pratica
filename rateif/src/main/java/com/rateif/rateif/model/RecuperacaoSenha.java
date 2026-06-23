package com.rateif.rateif.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRecuperacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String token;

    private LocalDateTime expiraEm;

    private LocalDateTime usadoEm;
}