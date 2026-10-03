package com.rateif.rateif.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rateif.rateif.dto.MobileDashboardResponse;
import com.rateif.rateif.repository.*;
import com.rateif.rateif.service.MobileTokenService;
import com.rateif.rateif.service.VdcService;

@RestController
@RequestMapping("/api/mobile/dashboard")
@CrossOrigin(origins = "*")
public class MobileDashboardController {
    private final UsuarioRepository usuarioRepository; private final AlunoRepository alunoRepository; private final TurmaRepository turmaRepository; private final AvaliacaoRepository avaliacaoRepository; private final RelatorioRepository relatorioRepository; private final VotacaoConselhoRepository votacaoRepository; private final MobileTokenService tokenService;
    public MobileDashboardController(UsuarioRepository usuarioRepository, AlunoRepository alunoRepository, TurmaRepository turmaRepository, AvaliacaoRepository avaliacaoRepository, RelatorioRepository relatorioRepository, VotacaoConselhoRepository votacaoRepository, MobileTokenService tokenService){this.usuarioRepository=usuarioRepository;this.alunoRepository=alunoRepository;this.turmaRepository=turmaRepository;this.avaliacaoRepository=avaliacaoRepository;this.relatorioRepository=relatorioRepository;this.votacaoRepository=votacaoRepository;this.tokenService=tokenService;}
    @GetMapping public ResponseEntity<?> dashboard(@RequestHeader(value="Authorization",required=false) String auth){
        try{int id=tokenService.validateBearer(auth).userId(); boolean ativo=usuarioRepository.findById(id).map(u->Boolean.TRUE.equals(u.getStatus())).orElse(false); if(!ativo)return unauthorized();}
        catch(MobileTokenService.InvalidMobileTokenException e){return unauthorized();}
        return ResponseEntity.ok(new MobileDashboardResponse(alunoRepository.count(),turmaRepository.count(),avaliacaoRepository.count(),relatorioRepository.count(),votacaoRepository.countByStatus(VdcService.ABERTA)));
    }
    private ResponseEntity<?> unauthorized(){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem","Sessão mobile inválida ou expirada."));}
}
