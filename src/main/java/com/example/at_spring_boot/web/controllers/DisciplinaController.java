package com.example.at_spring_boot.web.controllers;

import com.example.at_spring_boot.domain.Disciplina;
import com.example.at_spring_boot.service.DisciplinaService;
import com.example.at_spring_boot.web.dto.DisciplinaRequest;
import com.example.at_spring_boot.web.dto.DisciplinaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESSOR')")
    public DisciplinaResponse criar(@RequestBody DisciplinaRequest in) {
        Disciplina d = service.criar(in.getNome(), in.getCodigo());
        return toDTO(d);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSOR','ALUNO')")
    public List<DisciplinaResponse> listar() {
        return service.listarTodos().stream().map(this::toDTO).toList();
    }

    private DisciplinaResponse toDTO(Disciplina d) {
        var dto = new DisciplinaResponse();
        dto.setId(d.getId());
        dto.setNome(d.getNome());
        dto.setCodigo(d.getCodigo());
        return dto;
    }
}
