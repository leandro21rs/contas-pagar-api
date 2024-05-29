package com.cbyk.contas_pagar_api.service;

import com.cbyk.contas_pagar_api.model.Conta;
import com.cbyk.contas_pagar_api.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceImportacaoTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testImportarContas_Success() throws IOException {
        // Dados de exemplo do arquivo CSV
        String csvData = "2024-06-30,,100.00,Conta Teste,PENDENTE\n" +
                "2024-07-15,2024-07-15,150.00,Conta Teste 2,PAGA";

        MockMultipartFile file = new MockMultipartFile("file", "contas.csv", "text/csv", csvData.getBytes());

        // Mock do repositório para salvar as contas
        Conta conta1 = new Conta();
        conta1.setId(1L);
        conta1.setDataVencimento(LocalDate.of(2024, 6, 30));
        conta1.setValor(new BigDecimal("100.00"));
        conta1.setDescricao("Conta Teste");
        conta1.setSituacao("PENDENTE");

        Conta conta2 = new Conta();
        conta2.setId(2L);
        conta2.setDataVencimento(LocalDate.of(2024, 7, 15));
        conta2.setDataPagamento(LocalDate.of(2024, 7, 15));
        conta2.setValor(new BigDecimal("150.00"));
        conta2.setDescricao("Conta Teste 2");
        conta2.setSituacao("PAGA");

        when(contaRepository.saveAll(any())).thenReturn(Arrays.asList(conta1, conta2));

        // Chamar o serviço
        List<Conta> contasImportadas = contaService.importarContas(file);

        // Verificar resultados
        assertEquals(2, contasImportadas.size());
        assertEquals("Conta Teste", contasImportadas.get(0).getDescricao());
        assertEquals("Conta Teste 2", contasImportadas.get(1).getDescricao());

        // Verificar se o repositório foi chamado corretamente
        verify(contaRepository, times(1)).saveAll(any());
        verifyNoMoreInteractions(contaRepository);
    }
}
