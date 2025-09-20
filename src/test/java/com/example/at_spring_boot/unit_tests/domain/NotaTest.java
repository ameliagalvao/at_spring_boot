package com.example.at_spring_boot.unit_tests.domain;

import com.example.at_spring_boot.domain.Nota;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class NotaTest {

    @Test
    void of_quandoValorValido_deveCriarNota() {
        var n = Nota.of(8.5);
        assertThat(n.getValor()).isEqualTo(8.5);
        assertThat(n.aprovado()).isTrue();
    }

    @Test
    void of_quandoValorNegativo_deveLancarExcecao() {
        assertThatThrownBy(() -> Nota.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nota deve estar entre 0 e 10");
    }

    @Test
    void of_quandoValorMaiorQue10_deveLancarExcecao() {
        assertThatThrownBy(() -> Nota.of(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nota deve estar entre 0 e 10");
    }
}
