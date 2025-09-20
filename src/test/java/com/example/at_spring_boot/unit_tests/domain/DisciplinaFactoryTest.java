package com.example.at_spring_boot.unit_tests.domain;

import com.example.at_spring_boot.domain.DisciplinaFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class DisciplinaFactoryTest {

    @Test
    void nova_quandoCodigoComEspacos_deveNormalizar() {
        var factory = new DisciplinaFactory();
        var d = factory.nova("POO", " poo101 ");
        assertThat(d.getCodigo()).isEqualTo("POO101");
    }

    @Test
    void nova_quandoNomeVazio_deveLancarExcecao() {
        var factory = new DisciplinaFactory();
        assertThatThrownBy(() -> factory.nova("   ", "POO101"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Nome inválido");
    }

    @Test
    void nova_quandoCodigoNulo_deveLancarExcecao() {
        var factory = new DisciplinaFactory();
        assertThatThrownBy(() -> factory.nova("POO", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Código inválido");
    }
}
