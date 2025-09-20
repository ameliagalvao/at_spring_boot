package com.example.at_spring_boot.unit_tests.domain;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.domain.Boletim;
import com.example.at_spring_boot.domain.DisciplinaFactory;
import com.example.at_spring_boot.domain.Nota;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class BoletimTest {

    @Test
    void of_quandoCriadoSemNota_deveRetornarNullNoAprovado() {
        var aluno = Aluno.of("Leia", "123", "l@e.com", "999", "r1");
        var disc = new DisciplinaFactory().nova("POO", "POO101");

        var b = Boletim.of(aluno, disc);

        assertThat(b.getNota()).isNull();
    }

    @Test
    void setNota_quandoAtribuirNotaAprovado() {
        var aluno = Aluno.of("Leia", "123", "l@e.com", "999", "r1");
        var disc = new DisciplinaFactory().nova("POO", "POO101");
        var b = Boletim.of(aluno, disc);

        b.setNota(Nota.of(9.0));

        assertThat(b.getNota().aprovado()).isTrue();
    }

    @Test
    void setNota_quandoAtribuirNotaReprovado() {
        var aluno = Aluno.of("Luke", "222", "u@e.com", "888", "r2");
        var disc = new DisciplinaFactory().nova("BD", "BD101");
        var b = Boletim.of(aluno, disc);

        b.setNota(Nota.of(5.0));

        assertThat(b.getNota().aprovado()).isFalse();
    }
}
