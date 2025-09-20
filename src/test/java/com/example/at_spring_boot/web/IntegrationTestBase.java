package com.example.at_spring_boot.web;
import com.example.at_spring_boot.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    @Autowired protected BoletimRepository boletimRepo;
    @Autowired protected DocenciaRepository docenciaRepo;
    @Autowired protected AlunoRepository alunoRepo;
    @Autowired protected ProfessorRepository professorRepo;
    @Autowired protected DisciplinaRepository disciplinaRepo;

    @Autowired protected org.springframework.test.web.servlet.MockMvc mvc;
    @Autowired protected com.fasterxml.jackson.databind.ObjectMapper mapper;

    @BeforeEach
    void cleanDb() {
        // filhos primeiro (respeita FKs)
        boletimRepo.deleteAllInBatch();
        docenciaRepo.deleteAllInBatch();

        alunoRepo.deleteAllInBatch();
        professorRepo.deleteAllInBatch();
        disciplinaRepo.deleteAllInBatch();
    }
}