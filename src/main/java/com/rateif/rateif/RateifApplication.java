package com.rateif.rateif;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rateif.rateif.repository.AlunoRepository;
import com.rateif.rateif.repository.AvaliacaoRepository;
import com.rateif.rateif.repository.ProfessorRepository;
import com.rateif.rateif.repository.RelatorioRepository;
import com.rateif.rateif.repository.ResponsavelRepository;
import com.rateif.rateif.repository.TecnicoRepository;
import com.rateif.rateif.repository.TurmaRepository;
import com.rateif.rateif.repository.UsuarioRepository;

@SpringBootApplication
public class RateifApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateifApplication.class, args);
    }

    @Bean
    CommandLineRunner iniciar(
            UsuarioRepository usuarioRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository,
            TecnicoRepository tecnicoRepository,
            ResponsavelRepository responsavelRepository,
            TurmaRepository turmaRepository,
            AvaliacaoRepository avaliacaoRepository,
            RelatorioRepository relatorioRepository
    ) {

        return args -> {

            System.out.println("""
============================================================
                    RATEIF
        Sistema de Avaliação Escolar
============================================================
""");

            System.out.println("Usuarios: " + usuarioRepository.count());
            System.out.println("Alunos: " + alunoRepository.count());
            System.out.println("Professores: " + professorRepository.count());
            System.out.println("Tecnicos: " + tecnicoRepository.count());
            System.out.println("Responsaveis: " + responsavelRepository.count());
            System.out.println("Turmas: " + turmaRepository.count());
            System.out.println("Avaliacoes: " + avaliacaoRepository.count());
            System.out.println("Relatorios: " + relatorioRepository.count());



            
        };
    }
}