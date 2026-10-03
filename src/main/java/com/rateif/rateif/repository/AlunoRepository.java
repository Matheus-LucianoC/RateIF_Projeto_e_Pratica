package com.rateif.rateif.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rateif.rateif.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    long countByTurmaId(Integer turmaId);
    List<Aluno> findByTurmaIdOrderByMatriculaAsc(Integer turmaId);
}
