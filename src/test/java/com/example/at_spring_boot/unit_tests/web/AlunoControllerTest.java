package com.example.at_spring_boot.unit_tests.web;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.service.AlunoService;
import com.example.at_spring_boot.web.controllers.AlunoController;
import com.example.at_spring_boot.web.dto.AlunoRequest;
import com.example.at_spring_boot.web.dto.AlunoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlunoControllerTest {
    private static UsernamePasswordAuthenticationToken auth(String username, String... roles) {
        var auths = java.util.Arrays.stream(roles)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .toList();
        return new UsernamePasswordAuthenticationToken(username, "pwd", auths);
    }

    @Test
    void criar_deveChamarService_eRetornarResponseMapeado() {
        var service = mock(AlunoService.class);
        var controller = new AlunoController(service);

        var req = new AlunoRequest();
        req.setNome("Leia Organa");
        req.setCpf("123.456.789-00");
        req.setEmail("leia@e.com");
        req.setTelefone("81999990000");
        req.setEndereco("Rua do futuro, 123");

        var salvo = Aluno.of(req.getNome(), req.getCpf(), req.getEmail(), req.getTelefone(), req.getEndereco());
        salvo.setId(42L);
        when(service.criar(any(Aluno.class))).thenReturn(salvo);

        AlunoResponse res = controller.criar(req);

        verify(service).criar(any(Aluno.class));
        assertThat(res.getId()).isEqualTo(42L);
        assertThat(res.getNome()).isEqualTo("Leia Organa");
        assertThat(res.getCpf()).isEqualTo("123.456.789-00");
    }

    @Test
    void listar_deveMapearLista() {
        var service = mock(AlunoService.class);
        var controller = new AlunoController(service);

        var a1 = Aluno.of("Leia", "111", "l@e.com", "9", "r1"); a1.setId(1L);
        var a2 = Aluno.of("Luke", "222", "u@e.com", "9", "r2"); a2.setId(2L);
        when(service.listarTodos()).thenReturn(List.of(a1, a2));

        var lista = controller.listar();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getNome()).isEqualTo("Leia");
        assertThat(lista.get(1).getNome()).isEqualTo("Luke");
    }

    @Test
    void porId_professorPodeVerQualquerAluno() {
        var service = mock(AlunoService.class);
        var controller = new AlunoController(service);

        var aluno = Aluno.of("Leia", "111", "leia@e.com", "9", "r"); aluno.setId(7L);
        when(service.buscarPorId(7L)).thenReturn(aluno);

        var dto = controller.porId(7L, auth("prof@e.com", "PROFESSOR"));

        assertThat(dto.getEmail()).isEqualTo("leia@e.com");
    }

    @Test
    void porId_alunoPodeVerApenasSeusDados_senaoAccessDenied() {
        var service = mock(AlunoService.class);
        var controller = new AlunoController(service);

        var aluno = Aluno.of("Leia", "111", "leia@e.com", "9", "r"); aluno.setId(7L);
        when(service.buscarPorId(7L)).thenReturn(aluno);

        // mesmo email → OK
        assertThat(controller.porId(7L, auth("leia@e.com", "ALUNO")).getNome()).isEqualTo("Leia");

        // email diferente → AccessDeniedException
        assertThatThrownBy(() -> controller.porId(7L, auth("outra@e.com", "ALUNO")))
                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class);
    }

    @Test
    void criar_quandoServiceLancaIllegalArgument_propaga() {
        var service = mock(AlunoService.class);
        var controller = new AlunoController(service);

        var req = new AlunoRequest();
        req.setNome("Leia"); req.setCpf("111"); req.setEmail("l@e.com"); req.setTelefone("9"); req.setEndereco("r");

        when(service.criar(any(Aluno.class))).thenThrow(new IllegalArgumentException("duplicado"));

        assertThatThrownBy(() -> controller.criar(req))
                .isInstanceOf(IllegalArgumentException.class);
    }
}