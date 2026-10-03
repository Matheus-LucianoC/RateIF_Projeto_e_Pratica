package com.rateif.rateif.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.VotacaoConselho;

public interface VotacaoConselhoRepository extends JpaRepository<VotacaoConselho, Integer> {
    List<VotacaoConselho> findAllByOrderByCriadoEmDesc();
    Optional<VotacaoConselho> findFirstByStatusOrderByCriadoEmDesc(String status);
    boolean existsByTurmaIdAndStatus(Integer turmaId, String status);
    boolean existsByTurmaId(Integer turmaId);
    long countByStatus(String status);
}
