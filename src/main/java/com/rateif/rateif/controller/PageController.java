package com.rateif.rateif.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @Value("${google.client-id:}")
    private String googleClientId;

    @GetMapping({"/", "/login"})
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("googleClientId", googleClientId);
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(HttpSession session) {
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/dashboard";
        }
        return "cadastro";
    }

    @GetMapping("/recuperacao")
    public String recuperacao(HttpSession session) {
        if (session.getAttribute("usuarioId") != null) {
            return "redirect:/dashboard";
        }
        return "recuperacao";
    }

    @GetMapping("/recuperacao/nova")
    public String novaSenha(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token == null ? "" : token);
        return "nova-senha";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object nome = session.getAttribute("usuarioNome");
        Object email = session.getAttribute("usuarioEmail");

        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login";
        }

        model.addAttribute("nome", nome == null ? "Usuário" : nome);
        model.addAttribute("email", email == null ? "" : email);
        return "dashboard";
    }
}
