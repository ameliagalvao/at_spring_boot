package com.example.at_spring_boot.unit_tests.web;

import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.domain.DisciplinaFactory;
import com.example.at_spring_boot.service.DisciplinaService;
import com.example.at_spring_boot.web.controllers.DisciplinaController;
import com.example.at_spring_boot.web.dto.DisciplinaRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DisciplinaControllerTest {

    @Test
    void criar_deveChamarService_eRetornarResponse() {
        var service = mock(DisciplinaService.class);
        var controller = new DisciplinaController(service);

        var req = new DisciplinaRequest();
        req.setNome("POO");
        req.setCodigo("POO101");

        var salvo = new DisciplinaFactory().nova("POO", "POO101");
        salvo.setId(5L);
        when(service.criar("POO", "POO101")).thenReturn(salvo);

        var resp = controller.criar(req);

        verify(service).criar("POO", "POO101");
        assertThat(resp.getId()).isEqualTo(5L);
        assertThat(resp.getNome()).isEqualTo("POO");
        assertThat(resp.getCodigo()).isEqualTo("POO101");
    }

    @Test
    void listar_deveMapearLista() {
        var service = mock(DisciplinaService.class);
        var controller = new DisciplinaController(service);

        var f = new DisciplinaFactory();
        Disciplina d1 = f.nova("POO", "POO101"); d1.setId(1L);
        Disciplina d2 = f.nova("BD", "BD101");   d2.setId(2L);
        when(service.listarTodos()).thenReturn(List.of(d1, d2));

        var lista = controller.listar();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getCodigo()).isEqualTo("POO101");
        assertThat(lista.get(1).getCodigo()).isEqualTo("BD101");
    }

    @Test
    void criar_quandoServiceLancaIllegalArgument_propaga() {
        var service = mock(DisciplinaService.class);
        var controller = new DisciplinaController(service);

        var req = new DisciplinaRequest(); req.setNome("POO"); req.setCodigo("POO101");

        when(service.criar("POO", "POO101")).thenThrow(new IllegalArgumentException("duplicado"));

        assertThatThrownBy(() -> controller.criar(req))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
