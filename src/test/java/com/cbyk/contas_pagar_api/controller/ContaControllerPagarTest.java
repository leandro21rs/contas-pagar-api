package com.cbyk.contas_pagar_api.controller;

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

public class ContaControllerPagarTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPagarConta_Success() {
        Long idConta = 1L;
        Conta conta = new Conta();
        conta.setId(idConta);
        conta.setDataVencimento(LocalDate.of(2024, 12, 31));
        conta.setValor(new BigDecimal("200.00"));
        conta.setDescricao("Conta Teste");
        conta.setSituacao("PAGA");
        conta.setDataPagamento(LocalDate.now());

        Mockito.when(contaService.alterarSituacaoConta(eq(idConta)))
                .thenReturn(conta);

        ResponseEntity<Conta> response = contaController.pagarConta(idConta);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PAGA", response.getBody().getSituacao());
        assertEquals(LocalDate.now(), response.getBody().getDataPagamento());

        Mockito.verify(contaService, Mockito.times(1)).alterarSituacaoConta(eq(idConta));
        Mockito.verifyNoMoreInteractions(contaService);
    }

    @Test
    void testPagarConta_ContaNaoEncontrada() {
        Long idConta = 1L;

        Mockito.when(contaService.alterarSituacaoConta(eq(idConta)))
                .thenThrow(new IllegalArgumentException("Conta não encontrada com o ID fornecido"));

        ResponseEntity<Conta> response = contaController.pagarConta(idConta);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Mockito.verify(contaService, Mockito.times(1)).alterarSituacaoConta(eq(idConta));
        Mockito.verifyNoMoreInteractions(contaService);
    }
}
