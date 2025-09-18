package com.example.at_spring_boot.repository;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.domain.Boletim;
import com.example.at_spring_boot.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoletimRepository extends JpaRepository<Boletim, Long> {
    Optional<Boletim> findByAlunoAndDisciplina(Aluno aluno, Disciplina disciplina);
    List<Boletim> findByDisciplinaAndNota_ValorGreaterThanEqual(Disciplina d, double min);
    List<Boletim> findByDisciplinaAndNota_ValorLessThan(Disciplina d, double max);
}
