package com.example.at_spring_boot.web.dto;

public record BoletimResponse(
        Long id,
        Long alunoId,
        String alunoNome,
        String disciplinaCodigo,
        Double nota
) {}

