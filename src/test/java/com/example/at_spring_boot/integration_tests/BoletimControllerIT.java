package com.example.at_spring_boot.integration_tests;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoletimControllerIT extends IntegrationTestBase {

    private long seedAluno(String nome, String cpf) throws Exception {
        var req = """
          {"nome":"%s","cpf":"%s","email":"%s","telefone":"%s","endereco":"%s"}
        """.formatted(nome, cpf, "email@e.com", "999", "rua");
        var res = mvc.perform(post("/api/alunos")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated())
                .andReturn();
        var json = mapper.readTree(res.getResponse().getContentAsString());
        return json.get("id").asLong();
    }

    private void seedDisciplina(String nome, String codigo) throws Exception {
        var req = """
          {"nome":"%s","codigo":"%s"}
        """.formatted(nome, codigo);
        mvc.perform(post("/api/disciplinas")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated());
    }

    @Test
    void matricular_lancarNota_e_listarAprovados() throws Exception {
        long alunoId = seedAluno("Leia Organa", "123.456.789-00");
        seedDisciplina("POO", "POO101");

        // matricular
        var mat = """
          {"alunoId":%d,"codigoDisciplina":"%s"}
        """.formatted(alunoId, "POO101");

        mvc.perform(post("/api/boletins/matricular")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mat))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alunoId").value(alunoId))
                .andExpect(jsonPath("$.disciplinaCodigo").value("POO101"));

        // lançar nota
        var nota = """
          {"alunoId":%d,"codigoDisciplina":"%s","nota":8.0}
        """.formatted(alunoId, "POO101");

        mvc.perform(post("/api/boletins/nota")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nota))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota").value(8.0))
                .andExpect(jsonPath("$.aprovado").value(true));

        // aprovados
        mvc.perform(get("/api/boletins/aprovados")
                        .with(user("prof").roles("PROFESSOR"))
                        .param("codigoDisciplina", "POO101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alunoId").value(alunoId));
    }

    @Test
    void matricula_duplicada_retorna400() throws Exception {
        long alunoId = seedAluno("Leia Organa", "123.456.789-00");
        seedDisciplina("POO", "POO101");

        var mat = """
          {"alunoId":%d,"codigoDisciplina":"%s"}
        """.formatted(alunoId, "POO101");

        mvc.perform(post("/api/boletins/matricular")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mat))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/boletins/matricular")
                        .with(user("prof").roles("PROFESSOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mat))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }
}