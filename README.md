# RateIF

Sistema web e mobile para acompanhamento acadêmico e pedagógico.

## Módulos atuais

- Autenticação e recuperação de senha
- Painel inicial
- Sistema de Turmas: cadastro, edição, exclusão e consulta
- Conselho de Classe (VdC): criação de sessão, votação por aluno, resultado e encerramento
- Perfil do usuário
- API REST para o aplicativo mobile


## Backend

Requisitos: Java 17+ e MySQL em execução. O projeto possui Maven Wrapper.

No Windows, dentro da pasta que contém `mvnw.cmd`:

```powershell
.\mvnw.cmd clean spring-boot:run
```

Configuração padrão do banco:

- URL: `jdbc:mysql://localhost:3306/sistema_escolar`
- Usuário: `root`
- Senha: `root123`

Para mudar a configuração, use as variáveis `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.

## Mobile

Entre em `mobile/` e execute:

```powershell
npm install
npm start
```

A URL da API pode ser definida com `EXPO_PUBLIC_API_URL`.

## Testes

O ambiente de testes usa H2 em memória para não depender do MySQL local:

```powershell
.\mvnw.cmd test
```

## Estrutura

```text
RateIF_Projeto_e_Pratica-main/
├── src/
│   ├── main/java/
│   ├── main/resources/templates/
│   ├── main/resources/static/
│   └── test/
├── mobile/
├── pom.xml
└── mvnw.cmd
```

## Autores

Matheus Luciano de Caldas Figueiredo
Renan Aprigio Dias de Moura
