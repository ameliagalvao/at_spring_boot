package com.example.at_spring_boot.domain;

import org.springframework.stereotype.Component;

@Component
public class DisciplinaFactory {
    public Disciplina nova(String nome, String codigo) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome inválido.");
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Código inválido.");
        Disciplina d = new Disciplina();
        d.setNome(nome.trim());
        d.setCodigo(codigo.trim().toUpperCase());
        return d;
    }
}
