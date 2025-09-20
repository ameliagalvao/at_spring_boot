package com.example.at_spring_boot.integration_tests;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DisciplinaControllerIT extends IntegrationTestBase {

    @Test
    void criarDisciplina_201() throws Exception {
        var req = """
          {"nome":"POO","codigo":"POO101"}
        """;

        mvc.perform(post("/api/disciplinas")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.codigo").value("POO101"));
    }

    @Test
    void listar_200() throws Exception {
        var req = """
          {"nome":"POO","codigo":"POO101"}
        """;
        mvc.perform(post("/api/disciplinas")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/disciplinas")
                        .with(user("aluno").roles("ALUNO")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("POO101"));
    }

    @Test
    void criarDisciplina_negado_para_aluno_403() throws Exception {
        var req = """
          {"nome":"POO","codigo":"POO101"}
        """;
        mvc.perform(post("/api/disciplinas")
                        .with(user("aluno").roles("ALUNO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isForbidden());
    }
}