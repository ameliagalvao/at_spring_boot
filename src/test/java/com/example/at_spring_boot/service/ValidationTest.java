package com.example.at_spring_boot.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static com.example.at_spring_boot.service.Validation.*;

class ValidationTest {

    @Test
    void requireNonBlank_ok_e_erro() {
        requireNonBlank("x", "msg");
        assertThatThrownBy(() -> requireNonBlank(" ", "obrigatório"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("obrigatório");
    }

    @Test
    void requireEmail_ok_e_erro() {
        requireEmail("a@b.com", "inválido");
        assertThatThrownBy(() -> requireEmail("sem-arroba", "inválido"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inválido");
    }

    @Test
    void requireNonNull_ok_e_erro() {
        requireNonNull("x", "msg");
        assertThatThrownBy(() -> requireNonNull(null, "nulo"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo");
    }
}
