package com.example.at_spring_boot.web.controllers;

import com.example.at_spring_boot.domain.Docencia;
import com.example.at_spring_boot.service.DocenciaService;
import com.example.at_spring_boot.web.dto.DocenciaRequest;
import com.example.at_spring_boot.web.dto.DocenciaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docencias")
public class DocenciaController {

    private final DocenciaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESSOR')")
    public DocenciaResponse criar(@RequestBody DocenciaRequest dto) {
        Docencia d = service.alocar(dto.getProfessorId(), dto.getDisciplinaId(),
                dto.getPeriodo(), dto.getCargaHoraria(), dto.isPrincipal());
        return toDTO(d);
    }

    @GetMapping("/disciplina/{disciplinaId}")
    @PreAuthorize("hasAnyRole('PROFESSOR','ALUNO')")
    public List<DocenciaResponse> docentesDaDisciplina(@PathVariable Long disciplinaId,
                                                       @RequestParam String periodo) {
        return service.docentesDaDisciplina(disciplinaId, periodo).stream().map(this::toDTO).toList();
    }

    @GetMapping("/professor/{professorId}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public List<DocenciaResponse> disciplinasDoProfessor(@PathVariable Long professorId,
                                                         @RequestParam String periodo) {
        return service.disciplinasDoProfessor(professorId, periodo).stream().map(this::toDTO).toList();
    }

    @PostMapping("/{docenciaId}/encerrar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PROFESSOR')")
    public void encerrar(@PathVariable Long docenciaId) {
        service.encerrarAlocacao(docenciaId);
    }

    private DocenciaResponse toDTO(Docencia d) {
        var dto = new DocenciaResponse();
        dto.setId(d.getId());
        dto.setProfessorId(d.getProfessor().getId());
        dto.setProfessorNome(d.getProfessor().getNome());
        dto.setDisciplinaId(d.getDisciplina().getId());
        dto.setDisciplinaCodigo(d.getDisciplina().getCodigo());
        dto.setPeriodo(d.getPeriodo());
        dto.setCargaHoraria(d.getCargaHoraria());
        dto.setAtivo(d.isAtivo());
        dto.setPrincipal(d.isPrincipal());
        return dto;
    }
}
