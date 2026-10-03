package com.rateif.rateif.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.rateif.rateif.repository.*;
import com.rateif.rateif.service.VdcService;

@Controller
public class PageController {
    @Value("${google.client-id:}")
    private String googleClientId;

    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final RelatorioRepository relatorioRepository;
    private final VotacaoConselhoRepository votacaoRepository;

    public PageController(UsuarioRepository usuarioRepository, AlunoRepository alunoRepository, TurmaRepository turmaRepository,
                          AvaliacaoRepository avaliacaoRepository, RelatorioRepository relatorioRepository, VotacaoConselhoRepository votacaoRepository) {
        this.usuarioRepository = usuarioRepository; this.alunoRepository = alunoRepository; this.turmaRepository = turmaRepository;
        this.avaliacaoRepository = avaliacaoRepository; this.relatorioRepository = relatorioRepository; this.votacaoRepository = votacaoRepository;
    }

    @GetMapping({"/", "/login"})
    public String login(HttpSession session, Model model) {
        if (session.getAttribute("usuarioId") != null) return "redirect:/dashboard";
        model.addAttribute("googleClientId", googleClientId);
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(HttpSession session) { return session.getAttribute("usuarioId") != null ? "redirect:/dashboard" : "cadastro"; }

    @GetMapping("/recuperacao")
    public String recuperacao(HttpSession session) { return session.getAttribute("usuarioId") != null ? "redirect:/dashboard" : "recuperacao"; }

    @GetMapping("/recuperacao/nova")
    public String novaSenha(@RequestParam(required = false) String token, Model model) { model.addAttribute("token", token == null ? "" : token); return "nova-senha"; }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!autenticado(session)) return "redirect:/login";
        preencherUsuario(session, model);
        model.addAttribute("alunos", alunoRepository.count());
        model.addAttribute("turmas", turmaRepository.count());
        model.addAttribute("avaliacoes", avaliacaoRepository.count());
        model.addAttribute("relatorios", relatorioRepository.count());
        model.addAttribute("votacoesAbertas", votacaoRepository.countByStatus(VdcService.ABERTA));
        return "dashboard";
    }

   
    private boolean autenticado(HttpSession s) { return s.getAttribute("usuarioId") != null; }

    private void preencherUsuario(HttpSession session, Model model) {
        Object nome = session.getAttribute("usuarioNome"); Object email = session.getAttribute("usuarioEmail");
        String n = nome == null ? "Usuário" : nome.toString();
        model.addAttribute("nome", n); model.addAttribute("email", email == null ? "" : email); model.addAttribute("initial", n.substring(0, 1).toUpperCase());
        model.addAttribute("perfil", session.getAttribute("usuarioPerfil") == null ? "USUARIO" : session.getAttribute("usuarioPerfil"));
    }
}
