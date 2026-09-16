# Telas de autenticação do RateIF

Foram adicionadas as telas:

- `/login`
- `/cadastro`
- `/recuperacao`
- `/recuperacao/nova?token=...`
- `/dashboard`

O fluxo local usa BCrypt para armazenar as senhas. A recuperação gera um token de uso único com validade de 30 minutos. Para facilitar o teste acadêmico sem servidor de e-mail, o link é mostrado diretamente na tela de recuperação.

## Google Auth

A integração usa Google Identity Services no navegador. Configure um OAuth Client ID do tipo Web Application no Google Cloud e informe:

```properties
google.client-id=SEU_CLIENT_ID
```

ou, no Windows:

```powershell
$env:GOOGLE_CLIENT_ID="SEU_CLIENT_ID"
```

Depois reinicie o Spring Boot.

O backend valida a credencial recebida pelo endpoint oficial `https://oauth2.googleapis.com/tokeninfo` antes de criar/associar o usuário local.

Em produção, recomenda-se trocar a exibição direta do link de recuperação por envio por e-mail, aplicar proteção CSRF/rate limiting e separar os endpoints públicos da API administrativa.
