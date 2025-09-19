package com.example.at_spring_boot.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DocenciaResponse {
    private Long id;
    private Long professorId;
    private String professorNome;
    private Long disciplinaId;
    private String disciplinaCodigo;
    private String periodo;
    private Integer cargaHoraria;
    private boolean ativo;
    private boolean principal;
}