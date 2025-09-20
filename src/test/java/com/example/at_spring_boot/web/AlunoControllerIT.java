package com.example.at_spring_boot.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AlunoControllerIT extends IntegrationTestBase {
    @Test
    void criarAluno_201() throws Exception {
        var reqJson = """
          {"nome":"Leia Organa","cpf":"123.456.789-00","email":"leia@escola.com",
           "telefone":"81999990000","endereco":"Rua do futuro, 123"}
        """;

        mvc.perform(post("/api/alunos")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Leia Organa"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"));
    }

    @Test
    void listar_200() throws Exception {
        // seed
        var reqJson = """
          {"nome":"Leia Organa","cpf":"123.456.789-00","email":"leia@escola.com",
           "telefone":"81999990000","endereco":"Rua do futuro, 123"}
        """;
        mvc.perform(post("/api/alunos")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/alunos")
                        .with(user("prof").roles("PROFESSOR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Leia Organa"));
    }

    @Test
    void criarAluno_duplicado_retorna400() throws Exception {
        var req = """
          {"nome":"Leia Organa","cpf":"123.456.789-00","email":"leia@escola.com",
           "telefone":"81999990000","endereco":"Rua do futuro, 123"}
        """;
        mvc.perform(post("/api/alunos")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/alunos")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isBadRequest()) // mapeado pelo @RestControllerAdvice
                .andExpect(jsonPath("$.detail").exists());
    }
}