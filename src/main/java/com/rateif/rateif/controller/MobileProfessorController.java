package com.rateif.rateif.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rateif.rateif.service.MobileTokenService;
import com.rateif.rateif.repository.ProfessorRepository;

@RestController
@RequestMapping("/api/mobile/professores")
@CrossOrigin(origins = "*")
public class MobileProfessorController {
    private final MobileTokenService tokenService;
    private final ProfessorRepository repository;
    public MobileProfessorController(MobileTokenService tokenService, ProfessorRepository repository){this.tokenService=tokenService;this.repository=repository;}
    @GetMapping
    public ResponseEntity<?> listar(@RequestHeader(value="Authorization",required=false) String auth){
        try{tokenService.validateBearer(auth);}catch(Exception e){return ResponseEntity.status(401).body(Map.of("mensagem","Sessão mobile inválida ou expirada."));}
        List<Map<String,Object>> data=repository.findAll().stream().map(p -> Map.<String,Object>of("id",p.getId(),"nome",p.getUsuario()==null?"Professor #"+p.getId():p.getUsuario().getNome())).toList();
        return ResponseEntity.ok(data);
    }
}
