package com.example.at_spring_boot.service;

import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.domain.Docencia;
import com.example.at_spring_boot.domain.Professor;
import com.example.at_spring_boot.repository.DisciplinaRepository;
import com.example.at_spring_boot.repository.DocenciaRepository;
import com.example.at_spring_boot.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class DocenciaService {

    private final DocenciaRepository repo;
    private final DisciplinaRepository disciplinaRepo;
    private final ProfessorRepository professorRepo;

    @Transactional
    public Docencia alocar(Long professorId, Long disciplinaId, String periodo,
                           Integer cargaHoraria, boolean principal) {
        Professor prof = professorRepo.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));
        Disciplina disc = disciplinaRepo.findById(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada"));

        repo.findByProfessorAndDisciplinaAndPeriodo(prof, disc, periodo)
                .ifPresent(d -> { throw new IllegalArgumentException("Alocação já existe para esse período."); });

        Docencia d = Docencia.of(prof, disc, periodo, cargaHoraria, principal);
        return repo.save(d);
    }

    @Transactional(readOnly = true)
    public List<Docencia> docentesDaDisciplina(Long disciplinaId, String periodo) {
        Disciplina disc = disciplinaRepo.findById(disciplinaId)
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada"));
        return repo.findByDisciplinaAndPeriodoAndAtivoTrue(disc, periodo);
    }

    @Transactional(readOnly = true)
    public List<Docencia> disciplinasDoProfessor(Long professorId, String periodo) {
        Professor prof = professorRepo.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));
        return repo.findByProfessorAndPeriodoAndAtivoTrue(prof, periodo);
    }

    @Transactional
    public void encerrarAlocacao(Long docenciaId) {
        Docencia d = repo.findById(docenciaId)
                .orElseThrow(() -> new IllegalArgumentException("Docência não encontrada"));
        d.encerrar();
    }
}

