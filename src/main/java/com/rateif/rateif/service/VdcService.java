package com.rateif.rateif.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rateif.rateif.dto.VdcAlunoResponse;
import com.rateif.rateif.dto.VdcResponse;
import com.rateif.rateif.dto.VotacaoRequest;
import com.rateif.rateif.dto.VotoRequest;
import com.rateif.rateif.model.*;
import com.rateif.rateif.repository.*;

@Service
public class VdcService {
    public static final String ABERTA = "ABERTA";
    public static final String ENCERRADA = "ENCERRADA";

    private final VotacaoConselhoRepository votacaoRepository;
    private final VotoConselhoRepository votoRepository;
    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;

    public VdcService(VotacaoConselhoRepository votacaoRepository, VotoConselhoRepository votoRepository,
                      TurmaRepository turmaRepository, AlunoRepository alunoRepository, UsuarioRepository usuarioRepository) {
        this.votacaoRepository = votacaoRepository;
        this.votoRepository = votoRepository;
        this.turmaRepository = turmaRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<VdcResponse> listar(Integer usuarioId) {
        return votacaoRepository.findAllByOrderByCriadoEmDesc().stream().map(v -> toResponse(v, usuarioId)).toList();
    }

    @Transactional(readOnly = true)
    public VdcResponse detalhar(Integer id, Integer usuarioId) {
        return toResponse(votacaoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Votação não encontrada.")), usuarioId);
    }

    @Transactional
    public VdcResponse criar(VotacaoRequest request, Integer usuarioId) {
        if (request == null || request.turmaId() == null) throw new IllegalArgumentException("Selecione uma turma.");
        String titulo = request.titulo() == null ? "Conselho de Classe" : request.titulo().trim();
        if (titulo.isBlank()) titulo = "Conselho de Classe";
        if (titulo.length() > 140) throw new IllegalArgumentException("O título deve ter no máximo 140 caracteres.");
        validarUsuario(usuarioId);
        Turma turma = turmaRepository.findById(request.turmaId()).orElseThrow(() -> new IllegalArgumentException("Turma não encontrada."));
        if (votacaoRepository.existsByTurmaIdAndStatus(turma.getId(), ABERTA)) {
            throw new IllegalStateException("Já existe uma votação aberta para essa turma.");
        }
        VotacaoConselho v = new VotacaoConselho();
        v.setTitulo(titulo);
        v.setTurma(turma);
        v.setAnoLetivo(request.anoLetivo() == null ? turma.getAnoLetivo() : request.anoLetivo());
        v.setStatus(ABERTA);
        v.setCriadoEm(LocalDateTime.now());
        return toResponse(votacaoRepository.save(v), usuarioId);
    }

    @Transactional
    public VdcResponse votar(Integer votacaoId, VotoRequest request, Integer usuarioId) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuário não autenticado.");
        VotacaoConselho votacao = votacaoRepository.findById(votacaoId).orElseThrow(() -> new IllegalArgumentException("Votação não encontrada."));
        if (!ABERTA.equals(votacao.getStatus())) throw new IllegalStateException("Essa votação já foi encerrada.");
        if (request == null || request.alunoId() == null) throw new IllegalArgumentException("Selecione um aluno.");
        String decisao = normalizarDecisao(request.decisao());
        Aluno aluno = alunoRepository.findById(request.alunoId()).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
        if (aluno.getTurma() == null || !Objects.equals(aluno.getTurma().getId(), votacao.getTurma().getId())) {
            throw new IllegalArgumentException("O aluno não pertence à turma desta votação.");
        }
        Usuario usuario = validarUsuario(usuarioId);

        VotoConselho voto = votoRepository.findByVotacaoIdAndAlunoIdAndUsuarioId(votacaoId, aluno.getId(), usuarioId).orElseGet(VotoConselho::new);
        voto.setVotacao(votacao);
        voto.setAluno(aluno);
        voto.setUsuario(usuario);
        voto.setDecisao(decisao);
        voto.setComentario(blankToNull(request.comentario()));
        voto.setVotadoEm(LocalDateTime.now());
        votoRepository.save(voto);
        return toResponse(votacao, usuarioId);
    }

    @Transactional
    public VdcResponse encerrar(Integer id, Integer usuarioId) {
        VotacaoConselho v = votacaoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Votação não encontrada."));
        v.setStatus(ENCERRADA);
        v.setEncerradoEm(LocalDateTime.now());
        return toResponse(votacaoRepository.save(v), usuarioId);
    }

    private Usuario validarUsuario(Integer usuarioId) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuário não autenticado.");
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        if (!Boolean.TRUE.equals(usuario.getStatus())) throw new IllegalArgumentException("Usuário inativo.");
        return usuario;
    }

    private String normalizarDecisao(String decisao) {
        String d = decisao == null ? "" : decisao.trim().toUpperCase(Locale.ROOT);
        return switch (d) {
            case "APROVAR", "APROVADO" -> "APROVAR";
            case "RECUPERACAO", "RECUPERAÇÃO" -> "RECUPERACAO";
            case "RETER", "RETIDO" -> "RETER";
            case "ABSTENCAO", "ABSTENÇÃO" -> "ABSTENCAO";
            default -> throw new IllegalArgumentException("Decisão de voto inválida.");
        };
    }

    private VdcResponse toResponse(VotacaoConselho v, Integer usuarioId) {
        List<Aluno> alunos = alunoRepository.findByTurmaIdOrderByMatriculaAsc(v.getTurma().getId());
        List<VotoConselho> votos = votoRepository.findAllByVotacaoId(v.getId());
        Map<Integer, List<VotoConselho>> porAluno = votos.stream().collect(Collectors.groupingBy(x -> x.getAluno().getId()));
        List<VdcAlunoResponse> respostas = alunos.stream().map(a -> {
            List<VotoConselho> votosAluno = porAluno.getOrDefault(a.getId(), List.of());
            long aprovar = votosAluno.stream().filter(x -> "APROVAR".equals(x.getDecisao())).count();
            long recuperacao = votosAluno.stream().filter(x -> "RECUPERACAO".equals(x.getDecisao())).count();
            long reter = votosAluno.stream().filter(x -> "RETER".equals(x.getDecisao())).count();
            long abstencao = votosAluno.stream().filter(x -> "ABSTENCAO".equals(x.getDecisao())).count();
            String meuVoto = usuarioId == null ? null : votosAluno.stream().filter(x -> Objects.equals(x.getUsuario().getId(), usuarioId)).map(VotoConselho::getDecisao).findFirst().orElse(null);
            long max = Math.max(Math.max(aprovar, recuperacao), Math.max(reter, abstencao));
            String resultado;
            if (max == 0) resultado = "Sem votos";
            else {
                int lideres = 0;
                if (aprovar == max) lideres++;
                if (recuperacao == max) lideres++;
                if (reter == max) lideres++;
                if (abstencao == max) lideres++;
                resultado = lideres > 1 ? "Empate" : decisaoLabel(aprovar, recuperacao, reter, abstencao);
            }
            String nome = a.getUsuario() == null ? "Aluno #" + a.getId() : a.getUsuario().getNome();
            return new VdcAlunoResponse(a.getId(), nome, a.getMatricula(), aprovar, recuperacao, reter, abstencao, meuVoto, resultado);
        }).toList();
        return new VdcResponse(v.getId(), v.getTitulo(), v.getTurma().getId(), v.getTurma().getNomeTurma(), v.getAnoLetivo(), v.getStatus(), v.getCriadoEm(), v.getEncerradoEm(), respostas);
    }

    private String decisaoLabel(long a, long r, long t, long ab) {
        long max = Math.max(Math.max(a, r), Math.max(t, ab));
        if (a == max) return "Aprovar";
        if (r == max) return "Recuperação";
        if (t == max) return "Reter";
        return "Abstenção";
    }

    private String blankToNull(String text) { return text == null || text.isBlank() ? null : text.trim(); }
}
