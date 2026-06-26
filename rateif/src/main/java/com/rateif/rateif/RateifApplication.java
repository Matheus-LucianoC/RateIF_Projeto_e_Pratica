package com.rateif.rateif;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rateif.rateif.model.Aluno;
import com.rateif.rateif.model.Professor;
import com.rateif.rateif.model.Usuario;
import com.rateif.rateif.repository.AlunoRepository;
import com.rateif.rateif.repository.ProfessorRepository;
import com.rateif.rateif.repository.TecnicoRepository;
import com.rateif.rateif.repository.UsuarioRepository;

@SpringBootApplication
public class RateifApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateifApplication.class, args);
    }

    @Bean
CommandLineRunner run(
        UsuarioRepository usuarioRepository,
        AlunoRepository alunoRepository,
        ProfessorRepository professorRepository,
        TecnicoRepository tecnicoRepository
) {
    return args -> {

        if (usuarioRepository.count() == 0) {

            Usuario u1 = new Usuario();
            u1.setNome("Aluno User");
            u1.setEmail("aluno@email.com");
            u1.setSenhaHash("123");
            u1.setPerfil("ALUNO");
            u1.setStatus(true);
            u1.setCriadoEm(LocalDateTime.now());
            usuarioRepository.save(u1);

            Usuario u2 = new Usuario();
            u2.setNome("Professor User");
            u2.setEmail("prof@email.com");
            u2.setSenhaHash("123");
            u2.setPerfil("PROFESSOR");
            u2.setStatus(true);
            u2.setCriadoEm(LocalDateTime.now());
            usuarioRepository.save(u2);

            Aluno a = new Aluno();
            a.setMatricula("2026001");
            a.setIdade(17);
            a.setEmailInstitucional("aluno@if.com");
            a.setStatus(true);
            a.setUsuario(u1);
            alunoRepository.save(a);

            Professor p = new Professor();
            p.setUsuario(u2);
            professorRepository.save(p);

            System.out.println("Dados criados com sucesso!");
        }

        System.out.println("===== RESUMO DO SISTEMA =====");
        System.out.println("Usuarios: " + usuarioRepository.count());
        System.out.println("Alunos: " + alunoRepository.count());
        System.out.println("Professores: " + professorRepository.count());
        System.out.println("Tecnicos: " + tecnicoRepository.count());
    };
}
}