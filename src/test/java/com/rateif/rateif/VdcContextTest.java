package com.rateif.rateif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rateif.rateif.repository.VotacaoConselhoRepository;
import com.rateif.rateif.repository.VotoConselhoRepository;

@SpringBootTest
class VdcContextTest {
    @Autowired VotacaoConselhoRepository votacaoRepository;
    @Autowired VotoConselhoRepository votoRepository;

    @Test
    void repositoriosDoVdcDevemEstarDisponiveis() {
        assertTrue(votacaoRepository.count() >= 0);
        assertTrue(votoRepository.count() >= 0);
    }
}
