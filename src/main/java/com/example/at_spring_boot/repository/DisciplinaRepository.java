package com.example.at_spring_boot.repository;

import com.example.at_spring_boot.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    Optional<Disciplina> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
