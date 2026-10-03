package com.rateif.rateif.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @Column(name = "id_professor")
    private Integer id;

    private String formacao;

    @Column(name = "nivel_formacao")
    private String nivelFormacao;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_professor")
    private Usuario usuario;

    public Professor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getNivelFormacao() {
        return nivelFormacao;
    }

    public void setNivelFormacao(String nivelFormacao) {
        this.nivelFormacao = nivelFormacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}