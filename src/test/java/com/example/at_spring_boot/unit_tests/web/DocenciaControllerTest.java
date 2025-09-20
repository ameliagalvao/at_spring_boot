package com.example.at_spring_boot.unit_tests.web;

import com.example.at_spring_boot.domain.DisciplinaFactory;
import com.example.at_spring_boot.domain.Docencia;
import com.example.at_spring_boot.domain.Professor;
import com.example.at_spring_boot.service.DocenciaService;
import com.example.at_spring_boot.web.controllers.DocenciaController;
import com.example.at_spring_boot.web.dto.DocenciaRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DocenciaControllerTest {

    private static Docencia mkDocencia(long id, String profNome, String cod, String periodo, boolean principal) {
        var prof = Professor.of(profNome, "222", profNome.toLowerCase()+"@e.com", "9"); prof.setId(1L);
        var disc = new DisciplinaFactory().nova("POO", cod); disc.setId(10L);
        var d = Docencia.of(prof, disc, periodo, 60, principal); d.setId(id);
        return d;
    }

    @Test
    void criar_deveChamarService_eRetornarResponse() {
        var service = mock(DocenciaService.class);
        var controller = new DocenciaController(service);

        var req = new DocenciaRequest();
        req.setProfessorId(1L);
        req.setDisciplinaId(10L);
        req.setPeriodo("2025.1");
        req.setCargaHoraria(60);
        req.setPrincipal(true);

        var prof = Professor.of("Kenobi", "222", "k@e.com", "999"); prof.setId(1L);
        var disc = new DisciplinaFactory().nova("POO", "POO101");     disc.setId(10L);
        var salvo = Docencia.of(prof, disc, "2025.1", 60, true);      salvo.setId(100L);

        when(service.alocar(1L, 10L, "2025.1", 60, true)).thenReturn(salvo);

        var resp = controller.criar(req);

        verify(service).alocar(1L, 10L, "2025.1", 60, true);
        assertThat(resp.getId()).isEqualTo(100L);
        assertThat(resp.getProfessorNome()).isEqualTo("Kenobi");
        assertThat(resp.getDisciplinaCodigo()).isEqualTo("POO101");
        assertThat(resp.getPeriodo()).isEqualTo("2025.1");
    }

    @Test
    void listarPorDisciplina_e_porProfessor_devemMapearListas() {
        var service = mock(DocenciaService.class);
        var controller = new DocenciaController(service);

        var prof = Professor.of("Kenobi", "222", "k@e.com", "999"); prof.setId(1L);
        var disc = new DisciplinaFactory().nova("POO", "POO101");     disc.setId(10L);

        var d1 = Docencia.of(prof, disc, "2025.1", 60, true);  d1.setId(1L);
        var d2 = Docencia.of(Professor.of("Yoda", "333", "y@e.com","888"), disc, "2025.1", 40, false); d2.setId(2L);

        when(service.docentesDaDisciplina(10L, "2025.1")).thenReturn(List.of(d1, d2));
        when(service.disciplinasDoProfessor(1L, "2025.1")).thenReturn(List.of(d1));

        var porDisc = controller.docentesDaDisciplina(10L, "2025.1");
        var porProf = controller.disciplinasDoProfessor(1L, "2025.1");

        assertThat(porDisc).hasSize(2);
        assertThat(porDisc.get(0).getProfessorNome()).isEqualTo("Kenobi");

        assertThat(porProf).singleElement().satisfies(r -> {
            assertThat(r.getProfessorId()).isEqualTo(1L);
            assertThat(r.getDisciplinaCodigo()).isEqualTo("POO101");
        });
    }

    @Test
    void encerrar_deveChamarService() {
        var service = mock(DocenciaService.class);
        var controller = new DocenciaController(service);

        controller.encerrar(123L);
        verify(service).encerrarAlocacao(123L);
    }

    @Test
    void alocar_quandoDuplicado_propagaIllegalArgument() {
        var service = mock(DocenciaService.class);
        var controller = new DocenciaController(service);

        when(service.alocar(1L, 10L, "2025.1", 60, false))
                .thenThrow(new IllegalArgumentException("Alocação já existe"));

        var req = new DocenciaRequest();
        req.setProfessorId(1L);
        req.setDisciplinaId(10L);
        req.setPeriodo("2025.1");
        req.setCargaHoraria(60);
        req.setPrincipal(false);

        assertThatThrownBy(() -> controller.criar(req))
                .isInstanceOf(IllegalArgumentException.class);
    }
}