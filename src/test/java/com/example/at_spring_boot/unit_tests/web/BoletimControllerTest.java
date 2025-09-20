package com.example.at_spring_boot.unit_tests.web;

import com.example.at_spring_boot.domain.*;
import com.example.at_spring_boot.service.BoletimService;
import com.example.at_spring_boot.web.controllers.BoletimController;
import com.example.at_spring_boot.web.dto.AtribuirNotaRequest;
import com.example.at_spring_boot.web.dto.BoletimResponse;
import com.example.at_spring_boot.web.dto.MatricularRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoletimControllerTest {

    private Boletim mkBoletim(Long id, String alunoNome, String cod, Double nota) {
        var aluno = Aluno.of(alunoNome, "123", alunoNome+"@e.com", "9", "r"); aluno.setId(1L);
        var disc  = new DisciplinaFactory().nova("POO", cod);                      disc.setId(10L);
        var b = Boletim.of(aluno, disc); b.setId(id);
        if (nota != null) b.setNota(Nota.of(nota));
        return b;
    }

    @Test
    void matricular_deveMapearResponse_semNota() {
        var service = mock(BoletimService.class);
        var controller = new BoletimController(service);

        when(service.matricular(1L, "POO101"))
                .thenReturn(mkBoletim(100L, "Leia", "POO101", null));

        var req = new MatricularRequest(); // sem construtor com args
        req.setAlunoId(1L);
        req.setCodigoDisciplina("POO101");

        BoletimResponse resp = controller.matricular(req);

        verify(service).matricular(1L, "POO101");
        assertThat(resp.id()).isEqualTo(100L);
        assertThat(resp.alunoId()).isEqualTo(1L);
        assertThat(resp.alunoNome()).isEqualTo("Leia");
        assertThat(resp.disciplinaCodigo()).isEqualTo("POO101");
        assertThat(resp.nota()).isNull();
        assertThat(resp.aprovado()).isNull(); // sem nota não há aprovado/reprovado
    }

    @Test
    void atribuirNota_deveRetornarAprovado() {
        var service = mock(BoletimService.class);
        var controller = new BoletimController(service);

        when(service.atribuirNota(1L, "POO101", 8.0))
                .thenReturn(mkBoletim(100L, "Leia", "POO101", 8.0));

        var req = new AtribuirNotaRequest(); // use setters
        req.setAlunoId(1L);
        req.setCodigoDisciplina("POO101");
        req.setNota(8.0);

        BoletimResponse resp = controller.atribuirNota(req);

        verify(service).atribuirNota(1L, "POO101", 8.0);
        assertThat(resp.nota()).isEqualTo(8.0);
        assertThat(resp.aprovado()).isTrue();
    }

    @Test
    void aprovados_e_reprovados_deveMapearListas() {
        var service = mock(BoletimService.class);
        var controller = new BoletimController(service);

        when(service.aprovados("POO101")).thenReturn(
                List.of(mkBoletim(1L, "Leia", "POO101", 9.0)));
        when(service.reprovados("POO101")).thenReturn(
                List.of(mkBoletim(2L, "Luke", "POO101", 5.0)));

        var aps = controller.aprovados("POO101");
        var rps = controller.reprovados("POO101");

        assertThat(aps).singleElement().satisfies(b -> {
            assertThat(b.alunoNome()).isEqualTo("Leia");
            assertThat(b.nota()).isEqualTo(9.0);
            assertThat(b.aprovado()).isTrue();
        });

        assertThat(rps).singleElement().satisfies(b -> {
            assertThat(b.alunoNome()).isEqualTo("Luke");
            assertThat(b.nota()).isEqualTo(5.0);
            assertThat(b.aprovado()).isFalse();
        });
    }

    @Test
    void matricular_quandoDuplicado_propagaIllegalArgument() {
        var service = mock(BoletimService.class);
        var controller = new BoletimController(service);

        when(service.matricular(1L, "POO101")).thenThrow(new IllegalArgumentException("duplicado"));

        var req = new MatricularRequest(); req.setAlunoId(1L); req.setCodigoDisciplina("POO101");

        assertThatThrownBy(() -> controller.matricular(req))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
