package com.rateif.rateif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}