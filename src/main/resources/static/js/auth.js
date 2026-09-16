function showMessage(text, type = "error") {
    const element = document.getElementById("message");
    if (!element) return;
    element.className = `message show ${type}`;
    element.textContent = text;
}

function clearMessage() {
    const element = document.getElementById("message");
    if (!element) return;
    element.className = "message";
    element.textContent = "";
}

function togglePassword(id, button) {
    const input = document.getElementById(id);
    if (!input) return;

    input.type = input.type === "password" ? "text" : "password";
    button.textContent = input.type === "password" ? "Mostrar" : "Ocultar";
}

async function requestJson(url, body) {
    const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });

    const data = await response.json().catch(() => ({
        mensagem: "O servidor retornou uma resposta inválida."
    }));

    if (!response.ok) {
        throw new Error(data.mensagem || "Não foi possível concluir a operação.");
    }

    return data;
}

function initLogin() {
    const form = document.getElementById("loginForm");
    if (!form) return;

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearMessage();

        try {
            const data = await requestJson("/auth/login", {
                email: document.getElementById("email").value,
                senha: document.getElementById("senha").value
            });

            showMessage(data.mensagem, "success");
            setTimeout(() => window.location.href = data.redirect || "/dashboard", 400);
        } catch (error) {
            showMessage(error.message);
        }
    });
}

async function googleLogin(credential) {
    try {
        clearMessage();

        const data = await requestJson("/auth/google", { credential });
        showMessage(data.mensagem, "success");
        setTimeout(() => window.location.href = data.redirect || "/dashboard", 400);
    } catch (error) {
        showMessage(error.message);
    }
}

function initCadastro() {
    const form = document.getElementById("cadastroForm");
    if (!form) return;

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearMessage();

        try {
            const data = await requestJson("/auth/cadastro", {
                nome: document.getElementById("nome").value,
                email: document.getElementById("email").value,
                senha: document.getElementById("senha").value,
                confirmarSenha: document.getElementById("confirmarSenha").value
            });

            showMessage(data.mensagem, "success");
            setTimeout(() => window.location.href = data.redirect || "/dashboard", 500);
        } catch (error) {
            showMessage(error.message);
        }
    });
}

function initRecuperacao() {
    const form = document.getElementById("recuperacaoForm");
    if (!form) return;

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearMessage();

        const recoveryLink = document.getElementById("recoveryLink");
        recoveryLink.className = "recovery-link";
        recoveryLink.textContent = "";

        try {
            const data = await requestJson("/auth/recuperacao", {
                email: document.getElementById("email").value
            });

            showMessage(data.mensagem, "success");

            if (data.link) {
                recoveryLink.textContent = `${window.location.origin}${data.link}`;
                recoveryLink.className = "recovery-link show";
            }
        } catch (error) {
            showMessage(error.message);
        }
    });
}

function initNovaSenha(token) {
    const form = document.getElementById("novaSenhaForm");
    if (!form) return;

    if (!token) {
        showMessage("Token de recuperação ausente.");
        return;
    }

    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearMessage();

        try {
            const data = await requestJson(
                `/auth/recuperacao/nova?token=${encodeURIComponent(token)}`,
                {
                    senha: document.getElementById("senha").value,
                    confirmarSenha: document.getElementById("confirmarSenha").value
                }
            );

            showMessage(data.mensagem, "success");
            setTimeout(() => window.location.href = data.redirect || "/login", 700);
        } catch (error) {
            showMessage(error.message);
        }
    });
}

async function logout() {
    try {
        const data = await requestJson("/auth/logout", {});
        window.location.href = data.redirect || "/login";
    } catch (error) {
        window.location.href = "/login";
    }
}
