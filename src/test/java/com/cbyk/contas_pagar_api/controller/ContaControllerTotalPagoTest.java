package com.cbyk.contas_pagar_api.controller;

import com.cbyk.contas_pagar_api.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ContaControllerTotalPagoTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObterValorTotalPagoPorPeriodo() {
        // Dados de exemplo
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 12, 31);
        BigDecimal valorEsperado = new BigDecimal("250.00");

        // Simulando o serviço retornando o valor esperado
        when(contaService.calcularValorTotalPagoPorPeriodo(eq(dataInicio), eq(dataFim)))
                .thenReturn(valorEsperado);

        // Chamando o método no controlador
        ResponseEntity<BigDecimal> response = contaController.obterValorTotalPagoPorPeriodo(dataInicio, dataFim);

        // Verificando o status e o corpo da resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(valorEsperado, response.getBody());
    }
}
