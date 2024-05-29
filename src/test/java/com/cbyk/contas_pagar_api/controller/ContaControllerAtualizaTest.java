package com.cbyk.contas_pagar_api.controller;

import com.cbyk.contas_pagar_api.controller.ContaController;
import com.cbyk.contas_pagar_api.model.Conta;
import com.cbyk.contas_pagar_api.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ContaControllerAtualizaTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAtualizarConta_Success() {
        // Dados de exemplo
        Long idConta = 1L;
        Conta contaAtualizada = new Conta();
        contaAtualizada.setId(idConta);
        contaAtualizada.setDataVencimento(LocalDate.of(2024, 12, 31));
        contaAtualizada.setValor(new BigDecimal("200.00"));
        contaAtualizada.setDescricao("Conta Teste Atualizada");
        contaAtualizada.setSituacao("PAGA");

        // Simulando o serviço retornando a conta atualizada
        Mockito.when(contaService.atualizarConta(eq(idConta), any(Conta.class)))
                .thenReturn(contaAtualizada);

        // Chamando o método no controlador
        ResponseEntity<Conta> response = contaController.atualizarConta(idConta, contaAtualizada);

        // Verificando o status e o corpo da resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(idConta, response.getBody().getId());
        assertEquals("Conta Teste Atualizada", response.getBody().getDescricao());
        assertEquals("PAGA", response.getBody().getSituacao());
        assertEquals(new BigDecimal("200.00"), response.getBody().getValor());

        // Verificando se o serviço foi chamado corretamente
        Mockito.verify(contaService, Mockito.times(1)).atualizarConta(eq(idConta), any(Conta.class));
        Mockito.verifyNoMoreInteractions(contaService);
    }

    @Test
    void testAtualizarConta_ContaNaoEncontrada() {
        Long idConta = 1L;
        Conta contaAtualizada = new Conta();
        contaAtualizada.setId(idConta);

        // Simulando o serviço lançando uma exceção quando a conta não é encontrada
        Mockito.when(contaService.atualizarConta(eq(idConta), any(Conta.class)))
                .thenThrow(new IllegalArgumentException("Conta não encontrada com o ID fornecido"));

        // Chamando o método no controlador
        ResponseEntity<Conta> response = contaController.atualizarConta(idConta, contaAtualizada);

        // Verificando o status da resposta
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verificando se o serviço foi chamado corretamente
        Mockito.verify(contaService, Mockito.times(1)).atualizarConta(eq(idConta), any(Conta.class));
        Mockito.verifyNoMoreInteractions(contaService);
    }
}
