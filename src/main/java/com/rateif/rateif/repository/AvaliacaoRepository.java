package com.rateif.rateif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {
}