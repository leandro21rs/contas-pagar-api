package com.cbyk.contas_pagar_api.service;


import com.cbyk.contas_pagar_api.model.Conta;
import com.cbyk.contas_pagar_api.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Conta cadastrarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    public Page<Conta> buscarContasPorId(Long id, Pageable pageable) {
        return contaRepository.findById(id, pageable);
    }

    public List<Conta> buscarTodasContas() {
        // mesmo nao solicitado fiz a criação
        return contaRepository.findAll();
    }

    public Conta atualizarConta(Long id, Conta contaAtualizada) {
        Optional<Conta> optionalConta = contaRepository.findById(id);
        if (optionalConta.isEmpty()) {
            throw new IllegalArgumentException("Conta não encontrada com o ID fornecido");
        }

        Conta contaExistente = optionalConta.get();
        // Atualize apenas os campos que são permitidos de serem alterados
        contaExistente.setDataVencimento(contaAtualizada.getDataVencimento());
        contaExistente.setDataPagamento(contaAtualizada.getDataPagamento());
        contaExistente.setValor(contaAtualizada.getValor());
        contaExistente.setDescricao(contaAtualizada.getDescricao());
        contaExistente.setSituacao(contaAtualizada.getSituacao());
        // Adicione mais campos conforme necessário

        return contaRepository.save(contaExistente);
    }

    public void excluirConta(Long id) {
        // mesmo nao solicitado fiz a criação
        contaRepository.deleteById(id);
    }

    public Conta alterarSituacaoConta(Long id) {
        Optional<Conta> optionalConta = contaRepository.findById(id);
        if (optionalConta.isEmpty()) {
            throw new IllegalArgumentException("Conta não encontrada com o ID fornecido");
        }

        Conta contaExistente = optionalConta.get();
        contaExistente.setSituacao("PAGA");
        contaExistente.setDataPagamento(LocalDate.now());

        return contaRepository.save(contaExistente);
    }

    public Page<Conta> buscarContasComFiltros(LocalDate dataVencimento, String descricao, Pageable pageable) {
        if (dataVencimento != null && descricao != null) {
            return contaRepository.findByDataVencimentoAndDescricaoContaining(dataVencimento, descricao, pageable);
        } else if (dataVencimento != null) {
            return contaRepository.findByDataVencimento(dataVencimento, pageable);
        } else if (descricao != null) {
            return contaRepository.findByDescricaoContaining(descricao, pageable);
        } else {
            return contaRepository.findAll(pageable);
        }
    }

    public BigDecimal calcularValorTotalPagoPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<Conta> contasPagasNoPeriodo = contaRepository.findByDataPagamentoBetweenAndSituacao(dataInicio, dataFim, "PAGA");
        return contasPagasNoPeriodo.stream()
                .map(Conta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Conta> importarContas(MultipartFile file) throws IOException {
        List<Conta> contas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                Conta conta = new Conta();
                conta.setDataVencimento(LocalDate.parse(data[0].trim()));
                conta.setDataPagamento(data[1].trim().isEmpty() ? null : LocalDate.parse(data[1].trim()));
                conta.setValor(new BigDecimal(data[2].trim()));
                conta.setDescricao(data[3].trim());
                conta.setSituacao(data[4].trim());

                contas.add(conta);
            }
        }

        return contaRepository.saveAll(contas);
    }
}

