package com.example.at_spring_boot.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AtribuirNotaRequest {
    private Long alunoId;
    private String codigoDisciplina;
    private double nota;
}
