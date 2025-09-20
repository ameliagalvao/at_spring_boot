package com.example.at_spring_boot.service;
import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.domain.Boletim;
import com.example.at_spring_boot.repository.BoletimRepository;
import com.example.at_spring_boot.domain.DisciplinaFactory;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoletimServiceTest {

    @Test
    void matricular_e_lancarNota() {
        var repo = mock(BoletimRepository.class);
        var alunoService = mock(AlunoService.class);
        var disciplinaService = mock(DisciplinaService.class);
        var service = new BoletimService(repo, alunoService, disciplinaService);

        var aluno = Aluno.of("Leia", "123", "leia@e.com", "999", "rua");
        aluno.setId(1L);

        // cria Disciplina via FACTORY (evita new Disciplina() com construtor protected)
        var disc = new DisciplinaFactory().nova("POO", "POO101");
        disc.setId(10L);

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(disciplinaService.porCodigo("POO101")).thenReturn(disc);
        when(repo.findByAlunoAndDisciplina(aluno, disc)).thenReturn(Optional.empty());
        when(repo.save(any(Boletim.class))).thenAnswer(i -> {
            Boletim b = i.getArgument(0);
            b.setId(100L);
            return b;
        });

        var b = service.matricular(1L, "POO101");
        assertThat(b.getId()).isEqualTo(100L);
        assertThat(b.getAluno()).isEqualTo(aluno);
        assertThat(b.getDisciplina()).isEqualTo(disc);

        when(repo.findByAlunoAndDisciplina(aluno, disc)).thenReturn(Optional.of(b));

        var atualizado = service.atribuirNota(1L, "POO101", 8.0);
        assertThat(atualizado.getNota().getValor()).isEqualTo(8.0);
        assertThat(atualizado.getNota().aprovado()).isTrue();
    }

    @Test
    void matricular_duplicado_disparaErro() {
        var repo = mock(BoletimRepository.class);
        var alunoService = mock(AlunoService.class);
        var disciplinaService = mock(DisciplinaService.class);
        var service = new BoletimService(repo, alunoService, disciplinaService);

        var aluno = Aluno.of("Leia", "123", "leia@e.com", "999", "rua");
        var disc = new DisciplinaFactory().nova("POO", "POO101");

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(disciplinaService.porCodigo("POO101")).thenReturn(disc);
        when(repo.findByAlunoAndDisciplina(aluno, disc)).thenReturn(Optional.of(Boletim.of(aluno, disc)));

        assertThatThrownBy(() -> service.matricular(1L, "POO101"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Aluno já matriculado");
    }
}