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
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class ContaControllerListarTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarContas_SemFiltros() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.of(2024, 12, 31));
        conta.setValor(new BigDecimal("200.00"));
        conta.setDescricao("Conta Teste");
        conta.setSituacao("PENDENTE");

        Page<Conta> page = new PageImpl<>(Collections.singletonList(conta));
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(contaService.buscarContasComFiltros(null, null, pageable)).thenReturn(page);

        ResponseEntity<Page<Conta>> response = contaController.listarContas(null, null, 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());

        Mockito.verify(contaService, Mockito.times(1)).buscarContasComFiltros(null, null, pageable);
        Mockito.verifyNoMoreInteractions(contaService);
    }

    @Test
    void testListarContas_ComFiltros() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.of(2024, 12, 31));
        conta.setValor(new BigDecimal("200.00"));
        conta.setDescricao("Conta Teste");
        conta.setSituacao("PENDENTE");

        Page<Conta> page = new PageImpl<>(Collections.singletonList(conta));
        Pageable pageable = PageRequest.of(0, 10);

        LocalDate dataVencimento = LocalDate.of(2024, 12, 31);
        String descricao = "Teste";

        Mockito.when(contaService.buscarContasComFiltros(dataVencimento, descricao, pageable)).thenReturn(page);

        ResponseEntity<Page<Conta>> response = contaController.listarContas(dataVencimento, descricao, 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());

        Mockito.verify(contaService, Mockito.times(1)).buscarContasComFiltros(dataVencimento, descricao, pageable);
        Mockito.verifyNoMoreInteractions(contaService);
    }
}
