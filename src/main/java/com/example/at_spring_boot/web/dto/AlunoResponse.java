package com.example.at_spring_boot.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlunoResponse {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
}