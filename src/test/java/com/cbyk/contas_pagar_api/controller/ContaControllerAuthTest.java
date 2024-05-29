package com.cbyk.contas_pagar_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContaControllerAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void cadastrarContaWithoutAuthentication_thenUnauthorized() throws Exception {
        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dataVencimento\":\"2024-12-31\", \"valor\":100.0, \"descricao\":\"Conta Teste\", \"situacao\":\"PENDENTE\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void cadastrarContaWithAuthentication_thenCreated() throws Exception {
        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"dataVencimento\":\"2024-12-31\", \"valor\":100.0, \"descricao\":\"Conta Teste\", \"situacao\":\"PENDENTE\"}"))
                .andExpect(status().isCreated());
    }
}
