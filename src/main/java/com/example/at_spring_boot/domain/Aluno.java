package com.example.at_spring_boot.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String endereco;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Boletim> boletins = new HashSet<>();

    public static Aluno of(String nome, String cpf, String email, String telefone, String endereco) {
        Aluno a = new Aluno();            // pode usar porque está dentro da própria classe
        a.setNome(nome);
        a.setCpf(cpf);
        a.setEmail(email);
        a.setTelefone(telefone);
        a.setEndereco(endereco);
        return a;
    }
}
