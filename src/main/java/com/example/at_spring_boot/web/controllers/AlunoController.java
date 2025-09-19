package com.example.at_spring_boot.web.controllers;

import com.example.at_spring_boot.domain.Aluno;
import com.example.at_spring_boot.service.AlunoService;
import com.example.at_spring_boot.web.dto.AlunoRequest;
import com.example.at_spring_boot.web.dto.AlunoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESSOR')")
    public AlunoResponse criar(@RequestBody AlunoRequest in) {
        var a = Aluno.of(
                in.getNome(),
                in.getCpf(),
                in.getEmail(),
                in.getTelefone(),
                in.getEndereco()
        );
        return toResponse(service.criar(a));
    }

    @GetMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public List<AlunoResponse> listar() {
        return service.listarTodos().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR','ALUNO')")
    public AlunoResponse porId(@PathVariable Long id, Authentication auth) {
        var aluno = service.buscarPorId(id);
        var isProfessor = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"));
        if (!isProfessor && !aluno.getEmail().equalsIgnoreCase(auth.getName())) {
            throw new org.springframework.security.access.AccessDeniedException("Você só pode acessar seus próprios dados.");
        }
        return toResponse(aluno);
    }

    private AlunoResponse toResponse(Aluno a) {
        var dto = new AlunoResponse();
        dto.setId(a.getId());
        dto.setNome(a.getNome());
        dto.setCpf(a.getCpf());
        dto.setEmail(a.getEmail());
        dto.setTelefone(a.getTelefone());
        dto.setEndereco(a.getEndereco());
        return dto;
    }
}
