package com.example.at_spring_boot.service;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.repository.AlunoRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlunoServiceTest {

    @Test
    void criar_validaCampos_eImpedeDuplicados() {
        var repo = mock(AlunoRepository.class);
        var service = new AlunoService(repo);

        var a = Aluno.of("Leia", "123", "leia@e.com", "999", "rua x");
        when(repo.findByCpf("123")).thenReturn(java.util.Optional.empty());
        when(repo.findByEmail("leia@e.com")).thenReturn(java.util.Optional.empty());
        when(repo.save(any(Aluno.class))).thenAnswer(i -> i.getArgument(0));

        var salvo = service.criar(a);
        assertThat(salvo.getNome()).isEqualTo("Leia");

        when(repo.findByCpf("123")).thenReturn(java.util.Optional.of(a));
        assertThatThrownBy(() -> service.criar(a))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF já cadastrado");
    }
}
