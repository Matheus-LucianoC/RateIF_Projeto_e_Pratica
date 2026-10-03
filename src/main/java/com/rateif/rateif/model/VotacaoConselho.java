package com.rateif.rateif.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "votacao_conselho")
public class VotacaoConselho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_votacao")
    private Integer id;

    @Column(nullable = false, length = 140)
    private String titulo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_turma", nullable = false)
    private Turma turma;

    @Column(name = "ano_letivo", nullable = false)
    private Integer anoLetivo;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "encerrado_em")
    private LocalDateTime encerradoEm;

    public VotacaoConselho() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Turma getTurma() { return turma; }
    public void setTurma(Turma turma) { this.turma = turma; }
    public Integer getAnoLetivo() { return anoLetivo; }
    public void setAnoLetivo(Integer anoLetivo) { this.anoLetivo = anoLetivo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getEncerradoEm() { return encerradoEm; }
    public void setEncerradoEm(LocalDateTime encerradoEm) { this.encerradoEm = encerradoEm; }
}
