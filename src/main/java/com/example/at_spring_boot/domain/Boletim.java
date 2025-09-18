package com.example.at_spring_boot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_aluno_disciplina", columnNames = {"aluno_id","disciplina_id"}))
public class Boletim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) private Aluno aluno;
    @ManyToOne(optional = false) private Disciplina disciplina;

    @Embedded
    private Nota nota; // pode ser null até o lançamento
}
