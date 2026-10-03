# RateIF Mobile

Aplicativo React Native + Expo integrado ao backend Spring Boot do RateIF.

## Requisitos

- Node.js LTS
- npm
- Expo CLI via `npx`
- Backend do RateIF rodando

## Instalação no Windows

Dentro de `mobile/`:

```powershell
npm install
npm start
```

Para usar um celular na mesma rede do computador, defina a URL da API antes de iniciar:

```powershell
$env:EXPO_PUBLIC_API_URL="http://SEU_IP:8080"
npm start
```

Depois abra pelo Expo Go.

## Módulos

- Início
- Turmas
- Conselho de Classe (VdC)
- Perfil

O mobile usa os mesmos dados do backend e a mesma paleta visual do front web.
