package com.rateif.rateif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {
}