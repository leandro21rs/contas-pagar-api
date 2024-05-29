package com.cbyk.contas_pagar_api.controller;

import com.cbyk.contas_pagar_api.model.Conta;
import com.cbyk.contas_pagar_api.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

public class ContaControllerListarPorIdTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarContasPorId_Success() {
        Long idConta = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Conta conta = new Conta();
        conta.setId(idConta);
        conta.setDataVencimento(LocalDate.of(2024, 12, 31));
        conta.setValor(new BigDecimal("200.00"));
        conta.setDescricao("Conta Teste");
        conta.setSituacao("PAGA");

        Page<Conta> pageContas = new PageImpl<>(Collections.singletonList(conta), pageable, 1);
        Mockito.when(contaService.buscarContasPorId(eq(idConta), eq(pageable))).thenReturn(pageContas);

        ResponseEntity<Page<Conta>> response = contaController.listarContasPorId(idConta, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(idConta, response.getBody().getContent().get(0).getId());

        Mockito.verify(contaService, Mockito.times(1)).buscarContasPorId(eq(idConta), eq(pageable));
        Mockito.verifyNoMoreInteractions(contaService);
    }

    @Test
    void testListarContasPorId_NotFound() {
        Long idConta = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Conta> pageContas = Page.empty(pageable);
        Mockito.when(contaService.buscarContasPorId(eq(idConta), eq(pageable))).thenReturn(pageContas);

        ResponseEntity<Page<Conta>> response = contaController.listarContasPorId(idConta, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getTotalElements());

        Mockito.verify(contaService, Mockito.times(1)).buscarContasPorId(eq(idConta), eq(pageable));
        Mockito.verifyNoMoreInteractions(contaService);
    }
}
