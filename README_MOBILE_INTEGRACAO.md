# Atualização RateIF — Integração Mobile

Esta atualização adiciona um aplicativo **React Native + Expo** ao mesmo repositório do RateIF e cria uma API REST específica para o mobile dentro do **Spring Boot existente**.

## O que foi adicionado

```text
RateIF_Projeto_e_Pratica-main/
├── src/main/java/com/rateif/rateif/
│   ├── config/
│   │   ├── CorsConfig.java
│   │   └── PasswordEncoderConfig.java
│   ├── controller/
│   │   ├── MobileAuthController.java
│   │   ├── MobileProfileController.java
│   │   └── MobileDashboardController.java
│   ├── dto/
│   │   ├── MobileAuthResponse.java
│   │   ├── MobileDashboardResponse.java
│   │   ├── MobilePerfilRequest.java
│   │   └── MobileUserResponse.java
│   └── service/
│       └── MobileTokenService.java
├── mobile/
│   ├── App.js
│   ├── app.json
│   ├── eas.json
│   ├── package.json
│   ├── .env.example
│   └── src/
│       ├── api.js
│       ├── storage.js
│       ├── context/AuthContext.js
│       ├── components/
│       └── screens/
└── README_MOBILE_INTEGRACAO.md
```

## Arquitetura

O backend Spring Boot continua sendo o núcleo do sistema e o mesmo banco `sistema_escolar` continua sendo usado.

```text
React Native / Expo
        │
        │ HTTP + Bearer Token
        ▼
Spring Boot /api/mobile/*
        │
        ├── UsuarioRepository
        ├── AlunoRepository
        ├── TurmaRepository
        ├── AvaliacaoRepository
        └── RelatorioRepository
                │
                ▼
        MySQL / MariaDB
```

A autenticação mobile é **stateless**. A versão web continua usando a sessão HTTP já existente em `/auth/*`; o mobile usa token próprio em `/api/mobile/auth/*`.

## Endpoints mobile

### Autenticação

`POST /api/mobile/auth/login`

```json
{
  "email": "usuario@email.com",
  "senha": "123456"
}
```

`POST /api/mobile/auth/cadastro`

```json
{
  "nome": "Usuário Teste",
  "email": "usuario@email.com",
  "senha": "123456",
  "confirmarSenha": "123456"
}
```

`POST /api/mobile/auth/logout`

Usado pelo aplicativo para encerrar a sessão local.

### Perfil

`GET /api/mobile/profile`

Header:

```text
Authorization: Bearer SEU_TOKEN
```

`PUT /api/mobile/profile`

```json
{
  "nome": "Nome Atualizado",
  "email": "novo@email.com"
}
```

O endpoint retorna um novo token para refletir nome/e-mail atualizados.

### Dashboard

`GET /api/mobile/dashboard`

Retorna contagens reais do banco:

```json
{
  "alunos": 0,
  "turmas": 0,
  "avaliacoes": 0,
  "relatorios": 0
}
```

## Configuração do backend

Preencha em `src/main/resources/application.properties`:

```properties
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

Para produção, defina uma chave própria:

```text
MOBILE_JWT_SECRET=uma-chave-com-pelo-menos-32-caracteres
```

## Executando o backend

No Windows, dentro de `RateIF_Projeto_e_Pratica-main`:

```cmd
mvnw.cmd spring-boot:run
```

ou:

```cmd
mvnw.cmd clean package
java -jar target\rateif-0.0.1-SNAPSHOT.jar
```

O backend ficará, por padrão, em:

```text
http://localhost:8080
```

## Configurando o Expo

Entre em `mobile`:

```cmd
cd mobile
npm install
```

Copie `.env.example` para `.env` e coloque o IP da máquina que está executando o Spring Boot:

```env
EXPO_PUBLIC_API_URL=http://192.168.0.100:8080
```

Em aparelho físico, **não use `localhost`**: o `localhost` do aplicativo é o próprio telefone.

Inicie o Expo:

```cmd
npx expo start
```

Para produção, substitua `EXPO_PUBLIC_API_URL` por uma URL HTTPS pública do backend e configure essa variável no ambiente do EAS. Exemplo:

```cmd
eas env:set --name EXPO_PUBLIC_API_URL --value https://api.seudominio.com --environment production --visibility plaintext
```

O perfil `production` já aponta para o ambiente EAS `production`. Para `preview`, use o ambiente `preview`.

## EAS Build

O projeto usa Expo SDK 57. A documentação atual do Expo indica SDK 57 com React Native 0.86 e exige Node.js 22.13.x ou superior.

O aplicativo já possui `eas.json` com perfis `development`, `preview` e `production`.

Configure a conta EAS:

```cmd
npm install --global eas-cli
cd mobile
eas login
eas build:configure
```

Depois, para Android:

```cmd
eas build --platform android --profile preview
```

Para gerar APK de instalação direta, o perfil `preview-apk` está preparado com `buildType: apk`.

```cmd
eas build --platform android --profile preview-apk
```

Para produção:

```cmd
eas build --platform android --profile production
```

## Fluxo das telas

```text
Login
  ├── Cadastro
  └── Entrar
        ↓
      Menu
      ├── Resumo do sistema
      └── Perfil
             ↓
       Atualizar nome/e-mail
```

## Observações

- `Usuario`, `UsuarioRepository` e o banco já existentes continuam sendo usados.
- A versão web não precisa ser convertida para React Native.
- O mobile não acessa o MySQL diretamente.
- O token mobile não substitui a sessão usada pelo site.
- Não coloque senha do banco nem `MOBILE_JWT_SECRET` dentro do aplicativo Expo.
