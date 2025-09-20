package com.example.at_spring_boot.service;

import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.repository.DisciplinaRepository;
import com.example.at_spring_boot.domain.DisciplinaFactory;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisciplinaServiceTest {

    @Test
    void criar_normalizaCodigo_eImpedeDuplicado() {
        var repo = mock(DisciplinaRepository.class);
        var factory = new DisciplinaFactory();
        var service = new DisciplinaService(repo, factory);

        when(repo.existsByCodigo("POO101")).thenReturn(false);
        when(repo.save(any(Disciplina.class))).thenAnswer(i -> i.getArgument(0));

        var d = service.criar("POO", " poo101 ");
        assertThat(d.getCodigo()).isEqualTo("POO101");

        when(repo.existsByCodigo("POO101")).thenReturn(true);
        assertThatThrownBy(() -> service.criar("POO", "POO101"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Código já utilizado");
    }

    @Test
    void porCodigo_naoEncontrado_disparaErro() {
        var repo = mock(DisciplinaRepository.class);
        var service = new DisciplinaService(repo, new DisciplinaFactory());

        when(repo.findByCodigo("BD101")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.porCodigo("bd101"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Disciplina não encontrada");
    }

    @Test
    void listarTodos_ok() {
        var repo = mock(DisciplinaRepository.class);
        var service = new DisciplinaService(repo, new DisciplinaFactory());
        when(repo.findAll()).thenReturn(java.util.List.of());
        assertThat(service.listarTodos()).isEmpty();
        verify(repo).findAll();
    }
}