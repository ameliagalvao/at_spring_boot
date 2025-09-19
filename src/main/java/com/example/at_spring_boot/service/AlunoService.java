package com.example.at_spring_boot.service;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository repo;

    @Transactional
    public Aluno criar(Aluno a) {
        repo.findByCpf(a.getCpf()).ifPresent(x -> { throw new IllegalArgumentException("CPF já cadastrado"); });
        repo.findByEmail(a.getEmail()).ifPresent(x -> { throw new IllegalArgumentException("E-mail já cadastrado"); });
        return repo.save(a);
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarTodos() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
    }
}
