package com.rateif.rateif.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
