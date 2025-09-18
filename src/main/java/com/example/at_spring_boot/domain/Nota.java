package com.example.at_spring_boot.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nota {
    private double valor;

    private Nota(double v) { this.valor = v; }

    public static Nota of(double v) {
        if (v < 0 || v > 10) throw new IllegalArgumentException("Nota deve estar entre 0 e 10.");
        return new Nota(v);
    }

    public boolean aprovado() { return valor >= 7.0; }
}
