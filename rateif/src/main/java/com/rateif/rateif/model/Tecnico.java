package com.rateif.rateif.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
public class Tecnico {

    @Id
    private Integer idTecnico;

    private String crad;
    private String formacao;
    private String nivelFormacao;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_tecnico")
    private Usuario usuario;
}