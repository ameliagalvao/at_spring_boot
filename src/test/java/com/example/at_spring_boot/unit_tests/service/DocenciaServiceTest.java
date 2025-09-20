package com.example.at_spring_boot.unit_tests.service;

import com.example.at_spring_boot.domain.DisciplinaFactory;
import com.example.at_spring_boot.domain.Docencia;
import com.example.at_spring_boot.domain.Professor;
import com.example.at_spring_boot.repository.DisciplinaRepository;
import com.example.at_spring_boot.repository.DocenciaRepository;
import com.example.at_spring_boot.repository.ProfessorRepository;
import com.example.at_spring_boot.service.DocenciaService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocenciaServiceTest {

    @Test
    void alocar_ok_e_naoDuplicarPeriodo() {
        var repo = mock(DocenciaRepository.class);
        var discRepo = mock(DisciplinaRepository.class);
        var profRepo = mock(ProfessorRepository.class);
        var service = new DocenciaService(repo, discRepo, profRepo);

        var prof = Professor.of("Kenobi", "222", "kenobi@e.com", "999");
        prof.setId(1L);
        var disc = new DisciplinaFactory().nova("POO", "POO101");
        disc.setId(10L);

        when(profRepo.findById(1L)).thenReturn(Optional.of(prof));
        when(discRepo.findById(10L)).thenReturn(Optional.of(disc));
        when(repo.findByProfessorAndDisciplinaAndPeriodo(prof, disc, "2025.1"))
                .thenReturn(Optional.empty());
        when(repo.save(any(Docencia.class))).thenAnswer(i -> { Docencia d = i.getArgument(0); d.setId(100L); return d; });

        var d = service.alocar(1L, 10L, "2025.1", 60, true);
        assertThat(d.getId()).isEqualTo(100L);

        when(repo.findByProfessorAndDisciplinaAndPeriodo(prof, disc, "2025.1"))
                .thenReturn(Optional.of(d));
        assertThatThrownBy(() -> service.alocar(1L, 10L, "2025.1", 60, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Alocação já existe");
    }

    @Test
    void docentesDaDisciplina_disciplinaNaoExiste_disparaErro() {
        var repo = mock(DocenciaRepository.class);
        var discRepo = mock(DisciplinaRepository.class);
        var profRepo = mock(ProfessorRepository.class);
        var service = new DocenciaService(repo, discRepo, profRepo);

        when(discRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.docentesDaDisciplina(99L, "2025.1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Disciplina não encontrada");
    }

    @Test
    void disciplinasDoProfessor_professorNaoExiste_disparaErro() {
        var repo = mock(DocenciaRepository.class);
        var discRepo = mock(DisciplinaRepository.class);
        var profRepo = mock(ProfessorRepository.class);
        var service = new DocenciaService(repo, discRepo, profRepo);

        when(profRepo.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.disciplinasDoProfessor(77L, "2025.1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Professor não encontrado");
    }

    @Test
    void encerrarAlocacao_ok_e_idNaoEncontrado() {
        var repo = mock(DocenciaRepository.class);
        var discRepo = mock(DisciplinaRepository.class);
        var profRepo = mock(ProfessorRepository.class);
        var service = new DocenciaService(repo, discRepo, profRepo);

        var prof = Professor.of("Yoda", "333", "yoda@e.com", "888");
        var disc = new DisciplinaFactory().nova("BD", "BD101");
        var existente = Docencia.of(prof, disc, "2025.2", 40, false);
        existente.setId(200L);

        when(repo.findById(200L)).thenReturn(Optional.of(existente));

        service.encerrarAlocacao(200L);

        assertThat(existente.isAtivo()).isFalse();
        assertThat(existente.getFim()).isNotNull();

        when(repo.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.encerrarAlocacao(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Docência não encontrada");
    }
}