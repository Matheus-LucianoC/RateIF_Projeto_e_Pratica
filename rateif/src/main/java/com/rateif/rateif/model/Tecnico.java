package com.rateif.rateif.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tecnico")
public class Tecnico {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Usuario usuario;

    @Column(name = "crad")
    private String crad;

    @Column(name = "formacao")
    private String formacao;

    @Column(name = "nivel_formacao")
    private String nivelFormacao;

    public Tecnico() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCrad() {
        return crad;
    }

    public void setCrad(String crad) {
        this.crad = crad;
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

    @Override
    public String toString() {
        return "Tecnico{" +
                "id=" + id +
                ", nome='" + usuario.getNome() + '\'' +
                ", crad='" + crad + '\'' +
                ", formacao='" + formacao + '\'' +
                ", nivelFormacao='" + nivelFormacao + '\'' +
                '}';
    }
}