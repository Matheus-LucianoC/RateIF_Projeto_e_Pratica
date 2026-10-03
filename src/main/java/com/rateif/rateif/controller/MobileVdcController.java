package com.rateif.rateif.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rateif.rateif.dto.*;
import com.rateif.rateif.model.Usuario;
import com.rateif.rateif.repository.UsuarioRepository;
import com.rateif.rateif.service.MobileTokenService;
import com.rateif.rateif.service.VdcService;

@RestController
@RequestMapping("/api/mobile/vdc")
@CrossOrigin(origins = "*")
public class MobileVdcController {
    private final MobileTokenService tokenService; private final UsuarioRepository usuarioRepository; private final VdcService service;
    public MobileVdcController(MobileTokenService tokenService, UsuarioRepository usuarioRepository, VdcService service) { this.tokenService = tokenService; this.usuarioRepository = usuarioRepository; this.service = service; }
    @GetMapping public ResponseEntity<?> listar(@RequestHeader(value="Authorization", required=false) String auth) {
        Integer uid = userId(auth); if (uid == null) return unauthorized(); return ResponseEntity.ok(service.listar(uid));
    }
    @PostMapping public ResponseEntity<?> criar(@RequestHeader(value="Authorization", required=false) String auth, @RequestBody VotacaoRequest r) {
        Integer uid=userId(auth); if(uid==null)return unauthorized(); try{return ResponseEntity.status(201).body(service.criar(r,uid));}catch(IllegalStateException e){return conflict(e);}catch(IllegalArgumentException e){return bad(e);}
    }
    @PostMapping("/{id}/votos") public ResponseEntity<?> votar(@RequestHeader(value="Authorization", required=false) String auth,@PathVariable Integer id,@RequestBody VotoRequest r){
        Integer uid=userId(auth); if(uid==null)return unauthorized(); try{return ResponseEntity.ok(service.votar(id,r,uid));}catch(IllegalStateException e){return conflict(e);}catch(IllegalArgumentException e){return bad(e);}
    }
    @PostMapping("/{id}/encerrar") public ResponseEntity<?> encerrar(@RequestHeader(value="Authorization", required=false) String auth,@PathVariable Integer id){
        Integer uid=userId(auth); if(uid==null)return unauthorized(); try{return ResponseEntity.ok(service.encerrar(id,uid));}catch(IllegalArgumentException e){return bad(e);}
    }
    private Integer userId(String auth){try{int id=tokenService.validateBearer(auth).userId(); Usuario u=usuarioRepository.findById(id).orElse(null); return u!=null&&Boolean.TRUE.equals(u.getStatus())?id:null;}catch(Exception e){return null;}}
    private ResponseEntity<?> unauthorized(){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensagem","Sessão mobile inválida ou expirada."));}
    private ResponseEntity<?> bad(Exception e){return ResponseEntity.badRequest().body(Map.of("mensagem",e.getMessage()));}
    private ResponseEntity<?> conflict(Exception e){return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensagem",e.getMessage()));}
}
