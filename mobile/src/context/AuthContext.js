import React, { createContext, useContext, useEffect, useMemo, useState } from "react";

import { mobileApi } from "../api";
import { getToken, removeToken, saveToken } from "../storage";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(null);
  const [usuario, setUsuario] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let mounted = true;

    async function restore() {
      try {
        const storedToken = await getToken();
        if (!storedToken) return;

        const data = await mobileApi.perfil(storedToken);
        if (mounted) {
          setToken(storedToken);
          setUsuario(data);
        }
      } catch {
        await removeToken();
      } finally {
        if (mounted) setLoading(false);
      }
    }

    restore();
    return () => {
      mounted = false;
    };
  }, []);

  async function login(email, senha) {
    const data = await mobileApi.login(email, senha);
    await saveToken(data.token);
    setToken(data.token);
    setUsuario(data.usuario);
    return data;
  }

  async function cadastro(nome, email, senha, confirmarSenha) {
    const data = await mobileApi.cadastro(nome, email, senha, confirmarSenha);
    await saveToken(data.token);
    setToken(data.token);
    setUsuario(data.usuario);
    return data;
  }

  async function logout() {
    if (token) {
      try {
        await mobileApi.logout(token);
      } catch {
        // A limpeza local continua mesmo quando o backend já expirou o token.
      }
    }
    await removeToken();
    setToken(null);
    setUsuario(null);
  }

  async function atualizarPerfil(nome, email) {
    const data = await mobileApi.atualizarPerfil(token, nome, email);
    await saveToken(data.token);
    setToken(data.token);
    setUsuario(data.usuario);
    return data;
  }

  const value = useMemo(
    () => ({
      token,
      usuario,
      loading,
      autenticado: Boolean(token && usuario),
      login,
      cadastro,
      logout,
      atualizarPerfil
    }),
    [token, usuario, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth deve ser usado dentro de AuthProvider.");
  return context;
}
