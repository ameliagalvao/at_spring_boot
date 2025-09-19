package com.example.at_spring_boot.web.controllers;

import com.example.at_spring_boot.domain.Boletim;
import com.example.at_spring_boot.service.BoletimService;
import com.example.at_spring_boot.web.dto.AtribuirNotaRequest;
import com.example.at_spring_boot.web.dto.MatricularRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boletins")
public class BoletimController {

    private final BoletimService service;

    @PostMapping("/matricular")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFESSOR')")
    public Boletim matricular(@RequestBody MatricularRequest dto) {
        return service.matricular(dto.getAlunoId(), dto.getCodigoDisciplina());
    }

    @PostMapping("/nota")
    @PreAuthorize("hasRole('PROFESSOR')")
    public Boletim atribuirNota(@RequestBody AtribuirNotaRequest dto) {
        return service.atribuirNota(dto.getAlunoId(), dto.getCodigoDisciplina(), dto.getNota());
    }

    @GetMapping("/aprovados/{codigoDisciplina}")
    @PreAuthorize("hasAnyRole('PROFESSOR','ALUNO')")
    public List<Boletim> aprovados(@PathVariable String codigoDisciplina) {
        return service.aprovados(codigoDisciplina);
    }

    @GetMapping("/reprovados/{codigoDisciplina}")
    @PreAuthorize("hasAnyRole('PROFESSOR','ALUNO')")
    public List<Boletim> reprovados(@PathVariable String codigoDisciplina) {
        return service.reprovados(codigoDisciplina);
    }
}
