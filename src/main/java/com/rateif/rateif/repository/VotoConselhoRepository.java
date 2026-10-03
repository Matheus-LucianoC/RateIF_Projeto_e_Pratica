package com.rateif.rateif.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.VotoConselho;

public interface VotoConselhoRepository extends JpaRepository<VotoConselho, Integer> {
    Optional<VotoConselho> findByVotacaoIdAndAlunoIdAndUsuarioId(Integer votacaoId, Integer alunoId, Integer usuarioId);
    List<VotoConselho> findAllByVotacaoId(Integer votacaoId);
}
