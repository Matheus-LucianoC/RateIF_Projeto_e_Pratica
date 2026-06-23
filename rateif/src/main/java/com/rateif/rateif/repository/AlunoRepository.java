package com.rateif.rateif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
}