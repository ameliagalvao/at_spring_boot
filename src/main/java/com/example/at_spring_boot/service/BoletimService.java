package com.example.at_spring_boot.service;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.domain.Boletim;
import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.domain.Nota;
import com.example.at_spring_boot.repository.BoletimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class BoletimService {

    private final BoletimRepository repo;
    private final AlunoService alunoService;
    private final DisciplinaService disciplinaService;

    @Transactional
    public Boletim matricular(Long alunoId, String codigoDisciplina) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        Disciplina disc = disciplinaService.porCodigo(codigoDisciplina);
        repo.findByAlunoAndDisciplina(aluno, disc).ifPresent(b -> {
            throw new IllegalArgumentException("Aluno já matriculado na disciplina.");
        });
        var boletim = Boletim.of(aluno, disc);
        return repo.save(boletim);
    }

    @Transactional
    public Boletim atribuirNota(Long alunoId, String codigoDisciplina, double nota) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        Disciplina disc = disciplinaService.porCodigo(codigoDisciplina);
        Boletim b = repo.findByAlunoAndDisciplina(aluno, disc)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não está matriculado na disciplina."));
        b.setNota(Nota.of(nota));
        return repo.save(b);
    }

    @Transactional(readOnly = true)
    public List<Boletim> aprovados(String codigoDisciplina) {
        Disciplina d = disciplinaService.porCodigo(codigoDisciplina);
        return repo.findByDisciplinaAndNota_ValorGreaterThanEqual(d, 7.0);
    }

    @Transactional(readOnly = true)
    public List<Boletim> reprovados(String codigoDisciplina) {
        Disciplina d = disciplinaService.porCodigo(codigoDisciplina);
        return repo.findByDisciplinaAndNota_ValorLessThan(d, 7.0);
    }
}
