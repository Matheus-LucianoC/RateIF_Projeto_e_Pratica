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
@Table(name = "log_download_relatorio")
public class LogDownloadRelatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer idLog;

    @ManyToOne
    @JoinColumn(name = "id_relatorio")
    private Relatorio relatorio;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "data_download")
    private LocalDateTime dataDownload;

    @Column(name = "extensao")
    private String extensao;

    public LogDownloadRelatorio() {
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public Relatorio getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorio relatorio) {
        this.relatorio = relatorio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataDownload() {
        return dataDownload;
    }

    public void setDataDownload(LocalDateTime dataDownload) {
        this.dataDownload = dataDownload;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }
}
