package com.rateif.rateif;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rateif.rateif.repository.UsuarioRepository;

@Component
public class TesteBanco implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public TesteBanco(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("Conectado ao banco com sucesso!");
        System.out.println("Quantidade de usuários: " + usuarioRepository.count());
    }
}