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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "voto_conselho", uniqueConstraints = @UniqueConstraint(
        name = "uk_voto_conselho_votacao_aluno_usuario",
        columnNames = {"id_votacao", "id_aluno", "id_usuario"}))
public class VotoConselho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voto")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_votacao", nullable = false)
    private VotacaoConselho votacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 20)
    private String decisao;

    @Column(length = 500)
    private String comentario;

    @Column(name = "votado_em", nullable = false)
    private LocalDateTime votadoEm;

    public VotoConselho() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public VotacaoConselho getVotacao() { return votacao; }
    public void setVotacao(VotacaoConselho votacao) { this.votacao = votacao; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getDecisao() { return decisao; }
    public void setDecisao(String decisao) { this.decisao = decisao; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getVotadoEm() { return votadoEm; }
    public void setVotadoEm(LocalDateTime votadoEm) { this.votadoEm = votadoEm; }
}
