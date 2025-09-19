package com.example.at_spring_boot.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlunoRequest {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
}
