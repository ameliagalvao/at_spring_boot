package com.example.at_spring_boot.web.controllers;

import com.example.at_spring_boot.domain.Boletim;
import com.example.at_spring_boot.service.BoletimService;
import com.example.at_spring_boot.web.dto.AtribuirNotaRequest;
import com.example.at_spring_boot.web.dto.BoletimResponse;
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
    public BoletimResponse matricular(@RequestBody MatricularRequest dto) {
        Boletim b = service.matricular(dto.getAlunoId(), dto.getCodigoDisciplina());
        return toResponse(b);
    }

    @PostMapping("/nota")
    @PreAuthorize("hasRole('PROFESSOR')")
    public BoletimResponse atribuirNota(@RequestBody AtribuirNotaRequest dto) {
        Boletim b = service.atribuirNota(dto.getAlunoId(), dto.getCodigoDisciplina(), dto.getNota());
        return toResponse(b);
    }

    @GetMapping("/aprovados/{codigoDisciplina}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public List<BoletimResponse> aprovados(@PathVariable String codigoDisciplina) {
        return service.aprovados(codigoDisciplina).stream().map(this::toResponse).toList();
    }

    @GetMapping("/reprovados/{codigoDisciplina}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public List<BoletimResponse> reprovados(@PathVariable String codigoDisciplina) {
        return service.reprovados(codigoDisciplina).stream().map(this::toResponse).toList();
    }

    private BoletimResponse toResponse(Boletim b) {
        return new BoletimResponse(
                b.getId(),
                b.getAluno().getId(),
                b.getAluno().getNome(),
                b.getDisciplina().getCodigo(),
                b.getNota() == null ? null : b.getNota().getValor()
        );
    }

}
