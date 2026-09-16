CREATE DATABASE sistema_escolar;
USE sistema_escolar;

-- =========================
-- TABELA USUARIO
-- =========================

CREATE TABLE usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    status BOOLEAN NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- TABELA PROFESSOR
-- =========================

CREATE TABLE professor (
    id_professor INT PRIMARY KEY,
    formacao VARCHAR(100),
    nivel_formacao VARCHAR(100),

    FOREIGN KEY (id_professor)
        REFERENCES usuario(id_usuario)
);

-- =========================
-- TABELA TECNICO
-- =========================

CREATE TABLE tecnico (
    id_tecnico INT PRIMARY KEY,
    crad VARCHAR(50),
    formacao VARCHAR(100),
    nivel_formacao VARCHAR(100),

    FOREIGN KEY (id_tecnico)
        REFERENCES usuario(id_usuario)
);

-- =========================
-- TABELA RESPONSAVEL
-- =========================

CREATE TABLE responsavel (
    id_responsavel INT PRIMARY KEY,
    documentacao VARCHAR(100),

    FOREIGN KEY (id_responsavel)
        REFERENCES usuario(id_usuario)
);

-- =========================
-- TABELA TURMA
-- =========================

CREATE TABLE turma (
    id_turma INT PRIMARY KEY AUTO_INCREMENT,
    nome_turma VARCHAR(100),
    ano_letivo YEAR,
    turno VARCHAR(20),

    id_professor INT,

    FOREIGN KEY (id_professor)
        REFERENCES professor(id_professor)
);

-- =========================
-- TABELA ALUNO
-- =========================

CREATE TABLE aluno (
    id_aluno INT PRIMARY KEY AUTO_INCREMENT,

    id_usuario INT UNIQUE,
    matricula VARCHAR(50) UNIQUE NOT NULL,
    idade INT,
    email_institucional VARCHAR(100) UNIQUE,

    id_turma INT,
    id_responsavel INT,

    status BOOLEAN,

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    FOREIGN KEY (id_turma)
        REFERENCES turma(id_turma),

    FOREIGN KEY (id_responsavel)
        REFERENCES responsavel(id_responsavel)
);

-- =========================
-- TABELA AVALIACAO
-- =========================

CREATE TABLE avaliacao (
    id_avaliacao INT PRIMARY KEY AUTO_INCREMENT,

    id_aluno INT,
    id_professor INT,

    nota DECIMAL(5,2),
    comentario TEXT,
    data_avaliacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_aluno)
        REFERENCES aluno(id_aluno),

    FOREIGN KEY (id_professor)
        REFERENCES professor(id_professor)
);

-- =========================
-- TABELA RELATORIO
-- =========================

CREATE TABLE relatorio (
    id_relatorio INT PRIMARY KEY AUTO_INCREMENT,

    id_aluno INT,
    id_professor INT,

    titulo VARCHAR(100),
    descricao TEXT,
    formato VARCHAR(10),
    caminho_arquivo VARCHAR(255),

    data_geracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_aluno)
        REFERENCES aluno(id_aluno),

    FOREIGN KEY (id_professor)
        REFERENCES professor(id_professor)
);

-- =========================
-- TABELA LOG DOWNLOAD
-- =========================

CREATE TABLE log_download_relatorio (
    id_log INT PRIMARY KEY AUTO_INCREMENT,

    id_relatorio INT,
    id_usuario INT,

    data_download TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    extensao VARCHAR(10),

    FOREIGN KEY (id_relatorio)
        REFERENCES relatorio(id_relatorio),

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
);

-- =========================
-- TABELA RECUPERACAO SENHA
-- =========================

CREATE TABLE recuperacao_senha (
    id_recuperacao INT PRIMARY KEY AUTO_INCREMENT,

    id_usuario INT,
    token VARCHAR(255),

    expira_em DATETIME,
    usado_em DATETIME,

    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
);