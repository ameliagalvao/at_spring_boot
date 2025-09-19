package com.example.at_spring_boot.service;

import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.domain.DisciplinaFactory;
import com.example.at_spring_boot.repository.DisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class DisciplinaService {

    private final DisciplinaRepository repo;
    private final DisciplinaFactory factory;

    @Transactional
    public Disciplina criar(String nome, String codigo) {
        var d = factory.nova(nome, codigo);
        if (repo.existsByCodigo(d.getCodigo())) throw new IllegalArgumentException("Código já utilizado.");
        return repo.save(d);
    }

    @Transactional(readOnly = true)
    public List<Disciplina> listarTodos() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Disciplina porCodigo(String codigo) {
        return repo.findByCodigo(codigo.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Disciplina não encontrada"));
    }
}