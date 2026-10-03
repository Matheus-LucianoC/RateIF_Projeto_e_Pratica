package com.rateif.rateif.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rateif.rateif.dto.TurmaRequest;
import com.rateif.rateif.dto.TurmaResponse;
import com.rateif.rateif.model.Professor;
import com.rateif.rateif.model.Turma;
import com.rateif.rateif.repository.AlunoRepository;
import com.rateif.rateif.repository.ProfessorRepository;
import com.rateif.rateif.repository.TurmaRepository;
import com.rateif.rateif.repository.VotacaoConselhoRepository;

@Service
public class TurmaService {
    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final VotacaoConselhoRepository votacaoRepository;

    public TurmaService(TurmaRepository turmaRepository, ProfessorRepository professorRepository, AlunoRepository alunoRepository, VotacaoConselhoRepository votacaoRepository) {
        this.turmaRepository = turmaRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.votacaoRepository = votacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<TurmaResponse> listar() {
        return turmaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public TurmaResponse salvar(Integer id, TurmaRequest request) {
        if (request == null || request.nomeTurma() == null || request.nomeTurma().isBlank()) {
            throw new IllegalArgumentException("Informe o nome da turma.");
        }
        if (request.anoLetivo() == null || request.anoLetivo() < 2000 || request.anoLetivo() > 2200) {
            throw new IllegalArgumentException("Informe um ano letivo válido.");
        }
        if (request.turno() == null || request.turno().isBlank()) {
            throw new IllegalArgumentException("Informe o turno.");
        }
        Turma turma = id == null ? new Turma() : turmaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Turma não encontrada."));
        turma.setNomeTurma(request.nomeTurma().trim());
        turma.setAnoLetivo(request.anoLetivo());
        turma.setTurno(request.turno().trim());
        if (request.professorId() == null) {
            turma.setProfessor(null);
        } else {
            Professor professor = professorRepository.findById(request.professorId())
                    .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado."));
            turma.setProfessor(professor);
        }
        return toResponse(turmaRepository.save(turma));
    }

    @Transactional
    public void excluir(Integer id) {
        Turma turma = turmaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Turma não encontrada."));
        if (alunoRepository.countByTurmaId(id) > 0) {
            throw new IllegalStateException("Não é possível excluir uma turma que possui alunos vinculados.");
        }
        if (votacaoRepository.existsByTurmaId(id)) {
            throw new IllegalStateException("Não é possível excluir uma turma que possui sessões de Conselho de Classe vinculadas.");
        }
        turmaRepository.delete(turma);
    }

    private TurmaResponse toResponse(Turma turma) {
        Integer professorId = turma.getProfessor() == null ? null : turma.getProfessor().getId();
        String professorNome = turma.getProfessor() == null || turma.getProfessor().getUsuario() == null
                ? "Sem professor definido" : turma.getProfessor().getUsuario().getNome();
        return new TurmaResponse(
                turma.getId(), turma.getNomeTurma(), turma.getAnoLetivo(), turma.getTurno(),
                professorId, professorNome, alunoRepository.countByTurmaId(turma.getId()));
    }
}
