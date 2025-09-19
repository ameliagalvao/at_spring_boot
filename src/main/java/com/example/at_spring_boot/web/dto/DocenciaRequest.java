package com.example.at_spring_boot.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DocenciaRequest {
    private Long professorId;
    private Long disciplinaId;
    private String periodo; // "2025.1"
    private Integer cargaHoraria;
    private boolean principal;
}
