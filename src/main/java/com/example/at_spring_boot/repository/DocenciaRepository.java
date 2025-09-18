package com.example.at_spring_boot.repository;

import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.domain.Docencia;
import com.example.at_spring_boot.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocenciaRepository extends JpaRepository<Docencia, Long> {
    List<Docencia> findByDisciplinaAndPeriodoAndAtivoTrue(Disciplina d, String periodo);
    List<Docencia> findByProfessorAndPeriodoAndAtivoTrue(Professor p, String periodo);
    List<Docencia> findByDisciplina(Disciplina d);
    List<Docencia> findByProfessor(Professor p);
    Optional<Docencia> findByProfessorAndDisciplinaAndPeriodo(Professor p, Disciplina d, String periodo);
}
