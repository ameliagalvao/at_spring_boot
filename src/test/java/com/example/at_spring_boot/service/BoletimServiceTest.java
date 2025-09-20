package com.example.at_spring_boot.service;
import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.domain.Boletim;
import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.repository.BoletimRepository;
import com.example.at_spring_boot.domain.DisciplinaFactory;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import java.util.List;
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

        when(repo.findByAlunoAndDisciplina(aluno, disc)).thenReturn(Optional.of(b));
        when(repo.save(any(Boletim.class))).thenAnswer(i -> i.getArgument(0));

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

    @Test
    void atribuirNota_naoMatriculado_disparaErro() {
        var repo = mock(BoletimRepository.class);
        var alunoService = mock(AlunoService.class);
        var disciplinaService = mock(DisciplinaService.class);
        var service = new BoletimService(repo, alunoService, disciplinaService);

        var aluno = Aluno.of("Luke", "321", "luke@e.com", "888", "rua");
        var disc = new DisciplinaFactory().nova("BD", "BD101");

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(disciplinaService.porCodigo("BD101")).thenReturn(disc);
        when(repo.findByAlunoAndDisciplina(aluno, disc)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atribuirNota(1L, "BD101", 5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("não está matriculado");
    }

    @Test
    void aprovados_e_reprovados_delegamParaRepositorio() {
        var repo = mock(BoletimRepository.class);
        var alunoService = mock(AlunoService.class);
        var disciplinaService = mock(DisciplinaService.class);
        var service = new BoletimService(repo, alunoService, disciplinaService);

        Disciplina d = new DisciplinaFactory().nova("POO", "POO101");
        when(disciplinaService.porCodigo("POO101")).thenReturn(d);

        when(repo.findByDisciplinaAndNota_ValorGreaterThanEqual(d, 7.0)).thenReturn(List.of());
        when(repo.findByDisciplinaAndNota_ValorLessThan(d, 7.0)).thenReturn(List.of());

        assertThat(service.aprovados("POO101")).isEmpty();
        assertThat(service.reprovados("POO101")).isEmpty();

        verify(repo).findByDisciplinaAndNota_ValorGreaterThanEqual(d, 7.0);
        verify(repo).findByDisciplinaAndNota_ValorLessThan(d, 7.0);
    }

    @Test
    void matricular_validaParametrosObrigatorios() {
        var repo = mock(BoletimRepository.class);
        var service = new BoletimService(repo, mock(AlunoService.class), mock(DisciplinaService.class));

        assertThatThrownBy(() -> service.matricular(null, "POO101"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.matricular(1L, "  "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}