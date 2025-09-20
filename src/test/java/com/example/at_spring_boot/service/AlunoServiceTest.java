package com.example.at_spring_boot.service;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.repository.AlunoRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlunoServiceTest {

    @Test
    void criar_validaCampos_eImpedeDuplicados() {
        var repo = mock(AlunoRepository.class);
        var service = new AlunoService(repo);

        var a = Aluno.of("Leia", "123", "leia@e.com", "999", "rua x");
        when(repo.findByCpf("123")).thenReturn(Optional.empty());
        when(repo.findByEmail("leia@e.com")).thenReturn(Optional.empty());
        when(repo.save(any(Aluno.class))).thenAnswer(i -> i.getArgument(0));

        var salvo = service.criar(a);
        assertThat(salvo.getNome()).isEqualTo("Leia");

        when(repo.findByCpf("123")).thenReturn(Optional.of(a));
        assertThatThrownBy(() -> service.criar(a))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF já cadastrado");
    }

    @Test
    void criar_emailInvalido_disparaErro() {
        var repo = mock(AlunoRepository.class);
        var service = new AlunoService(repo);

        var a = Aluno.of("Nome", "123", "email-invalido", "999", "rua");
        assertThatThrownBy(() -> service.criar(a))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("E-mail inválido");
    }

    @Test
    void buscarPorId_naoEncontrado_disparaErro() {
        var repo = mock(AlunoRepository.class);
        var service = new AlunoService(repo);

        when(repo.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscarPorId(9L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aluno não encontrado");
    }

    @Test
    void listarTodos_ok() {
        var repo = mock(AlunoRepository.class);
        var service = new AlunoService(repo);

        when(repo.findAll()).thenReturn(java.util.List.of());
        assertThat(service.listarTodos()).isEmpty();
        verify(repo).findAll();
    }
}