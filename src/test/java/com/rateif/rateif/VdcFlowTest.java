package com.rateif.rateif;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.rateif.rateif.dto.VdcResponse;
import com.rateif.rateif.dto.VotacaoRequest;
import com.rateif.rateif.dto.VotoRequest;
import com.rateif.rateif.model.Aluno;
import com.rateif.rateif.model.Professor;
import com.rateif.rateif.model.Turma;
import com.rateif.rateif.model.Usuario;
import com.rateif.rateif.repository.AlunoRepository;
import com.rateif.rateif.repository.ProfessorRepository;
import com.rateif.rateif.repository.TurmaRepository;
import com.rateif.rateif.repository.UsuarioRepository;
import com.rateif.rateif.repository.VotacaoConselhoRepository;
import com.rateif.rateif.repository.VotoConselhoRepository;
import com.rateif.rateif.service.VdcService;

@SpringBootTest
@Transactional
class VdcFlowTest {
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired ProfessorRepository professorRepository;
    @Autowired TurmaRepository turmaRepository;
    @Autowired AlunoRepository alunoRepository;
    @Autowired VotacaoConselhoRepository votacaoRepository;
    @Autowired VotoConselhoRepository votoRepository;
    @Autowired VdcService vdcService;

    @Test
    void deveCriarVotacaoRegistrarEAlterarVotoSemDuplicar() {
        Usuario participante = usuario("Participante", "participante@teste.local");
        Usuario alunoUsuario = usuario("Aluno Teste", "aluno@teste.local");

        Professor professor = new Professor();
        professor.setId(participante.getId());
        professor.setUsuario(participante);
        professorRepository.save(professor);

        Turma turma = new Turma();
        turma.setNomeTurma("3B DS");
        turma.setAnoLetivo(2026);
        turma.setTurno("Tarde");
        turma.setProfessor(professor);
        turma = turmaRepository.save(turma);

        Aluno aluno = new Aluno();
        aluno.setUsuario(alunoUsuario);
        aluno.setMatricula("2026001");
        aluno.setIdade(17);
        aluno.setEmailInstitucional("aluno@teste.local");
        aluno.setTurma(turma);
        aluno.setStatus(true);
        aluno = alunoRepository.save(aluno);

        VdcResponse votacao = vdcService.criar(new VotacaoRequest(turma.getId(), "Conselho 3B", 2026), participante.getId());
        assertNotNull(votacao.id());
        assertEquals(1, votacao.alunos().size());

        vdcService.votar(votacao.id(), new VotoRequest(aluno.getId(), "APROVAR", null), participante.getId());
        vdcService.votar(votacao.id(), new VotoRequest(aluno.getId(), "RECUPERACAO", null), participante.getId());

        assertEquals(1, votoRepository.findAllByVotacaoId(votacao.id()).size());
        VdcResponse atualizada = vdcService.detalhar(votacao.id(), participante.getId());
        assertEquals("RECUPERACAO", atualizada.alunos().get(0).meuVoto());
        assertEquals(1, atualizada.alunos().get(0).recuperacao());
    }

    private Usuario usuario(String nome, String email) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenhaHash("hash");
        u.setPerfil("USUARIO");
        u.setStatus(true);
        return usuarioRepository.save(u);
    }
}
