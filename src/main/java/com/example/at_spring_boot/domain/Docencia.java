package com.example.at_spring_boot.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "docencia",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_prof_disc_periodo",
                columnNames = {"professor_id","disciplina_id","periodo"}))
public class Docencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Professor professor;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;

    @Column(nullable = false, length = 16)
    private String periodo; // ex.: "2025.1"

    private LocalDate inicio;
    private LocalDate fim;
    private Integer cargaHoraria;
    private boolean ativo = true;
    private boolean principal = false;
}
