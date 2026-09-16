package com.rateif.rateif.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rateif.rateif.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
}