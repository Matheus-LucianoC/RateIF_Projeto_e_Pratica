package com.rateif.rateif.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.RecuperacaoSenha;

public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Integer> {

    Optional<RecuperacaoSenha> findByToken(String token);
}
