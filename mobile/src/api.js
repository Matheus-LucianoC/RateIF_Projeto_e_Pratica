const API_URL = (process.env.EXPO_PUBLIC_API_URL || "http://192.168.0.100:8080").replace(/\/$/, "");

async function request(path, options = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: { "Content-Type": "application/json", ...(options.headers || {}) }
  });
  const text = await response.text();
  let data = {};
  if (text) { try { data = JSON.parse(text); } catch { data = { mensagem: "O servidor retornou uma resposta inválida." }; } }
  if (!response.ok) { const error = new Error(data.mensagem || `Erro HTTP ${response.status}`); error.status = response.status; throw error; }
  return data;
}

export const mobileApi = {
  get baseUrl() { return API_URL; },
  login(email, senha) { return request("/api/mobile/auth/login", { method:"POST", body:JSON.stringify({email,senha}) }); },
  cadastro(nome,email,senha,confirmarSenha) { return request("/api/mobile/auth/cadastro", { method:"POST", body:JSON.stringify({nome,email,senha,confirmarSenha}) }); },
  logout(token) { return request("/api/mobile/auth/logout", { method:"POST", headers:{Authorization:`Bearer ${token}`} }); },
  perfil(token) { return request("/api/mobile/profile", { method:"GET", headers:{Authorization:`Bearer ${token}`} }); },
  atualizarPerfil(token,nome,email) { return request("/api/mobile/profile", { method:"PUT", headers:{Authorization:`Bearer ${token}`}, body:JSON.stringify({nome,email}) }); },
  dashboard(token) { return request("/api/mobile/dashboard", { method:"GET", headers:{Authorization:`Bearer ${token}`} }); },
  turmas(token) { return request("/api/mobile/turmas", { method:"GET", headers:{Authorization:`Bearer ${token}`} }); },
  criarTurma(token,body) { return request("/api/mobile/turmas", { method:"POST", headers:{Authorization:`Bearer ${token}`}, body:JSON.stringify(body) }); },
  atualizarTurma(token,id,body) { return request(`/api/mobile/turmas/${id}`, { method:"PUT", headers:{Authorization:`Bearer ${token}`}, body:JSON.stringify(body) }); },
  excluirTurma(token,id) { return request(`/api/mobile/turmas/${id}`, { method:"DELETE", headers:{Authorization:`Bearer ${token}`} }); },
  professores(token) { return request("/api/mobile/professores", { method:"GET", headers:{Authorization:`Bearer ${token}`} }); },
  vdc(token) { return request("/api/mobile/vdc", { method:"GET", headers:{Authorization:`Bearer ${token}`} }); },
  criarVdc(token,body) { return request("/api/mobile/vdc", { method:"POST", headers:{Authorization:`Bearer ${token}`}, body:JSON.stringify(body) }); },
  votarVdc(token,id,body) { return request(`/api/mobile/vdc/${id}/votos`, { method:"POST", headers:{Authorization:`Bearer ${token}`}, body:JSON.stringify(body) }); },
  encerrarVdc(token,id) { return request(`/api/mobile/vdc/${id}/encerrar`, { method:"POST", headers:{Authorization:`Bearer ${token}`}, body:"{}" }); }
};
