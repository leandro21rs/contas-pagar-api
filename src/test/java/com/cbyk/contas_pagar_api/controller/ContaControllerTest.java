package com.cbyk.contas_pagar_api.controller;



import com.cbyk.contas_pagar_api.model.Conta;
import com.cbyk.contas_pagar_api.repository.ContaRepository;
import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class ContaControllerTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaController contaController;

    @Test
    void cadastrarConta_DeveRetornarStatusCreated() {
        // Arrange
        Conta novaConta = new Conta();
        novaConta.setDataVencimento(LocalDate.now().plusDays(7));
        novaConta.setValor(BigDecimal.valueOf(100.00));
        novaConta.setDescricao("Conta de Água");
        novaConta.setSituacao("pendente");

        when(contaRepository.save(novaConta)).thenReturn(novaConta);

        // Act
        ResponseEntity<Conta> resposta = contaController.cadastrarConta(novaConta);

        // Assert
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
    }




}

