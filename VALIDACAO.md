# Validação do projeto

## Executado neste ambiente

- Checagem de sintaxe JavaScript em todos os arquivos `.js` do mobile: passou.
- Checagem de JSON em `mobile/package.json` e `mobile/app.json`: passou.
- `npm install --package-lock-only --ignore-scripts --no-audit --no-fund`: passou.
- Checagem estrutural dos templates web e dos caminhos dos assets: passou.
- Checagem dos endpoints de Turmas e VdC: passou.
- Checagem da paleta azul/roxo compartilhada entre web e mobile: passou.
- Checagem de remoção da implementação antiga de board/Trello/Kanban: passou.
- Checagem sintática orientada por `javac`: nenhum diagnóstico de parser foi encontrado.

## Testes de backend incluídos

O projeto agora inclui testes Spring Boot com H2 em memória, incluindo um fluxo de VdC que verifica criação de votação, registro de voto e alteração do voto sem duplicação.

No Windows, execute dentro da pasta do backend:

```powershell
.\\mvnw.cmd test
```

## Limitação deste ambiente

O teste Maven completo não pôde ser executado aqui porque o Maven Wrapper tentou baixar a distribuição do Maven e a rede do ambiente bloqueou o download. Por isso, nenhum resultado de `mvn test` foi inventado como se tivesse sido executado.
