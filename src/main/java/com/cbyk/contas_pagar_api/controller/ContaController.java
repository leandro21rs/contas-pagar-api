package com.cbyk.contas_pagar_api.controller;


import com.cbyk.contas_pagar_api.model.Conta;
import com.cbyk.contas_pagar_api.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
        Conta novaConta = contaService.cadastrarConta(conta);
        return new ResponseEntity<>(novaConta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody Conta contaAtualizada) {
        try {
            Conta conta = contaService.atualizarConta(id, contaAtualizada);
            return ResponseEntity.ok(conta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<Conta> pagarConta(@PathVariable Long id) {
        try {
            Conta conta = contaService.alterarSituacaoConta(id);
            return ResponseEntity.ok(conta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Conta>> listarContas(
            @RequestParam(required = false) LocalDate dataVencimento,
            @RequestParam(required = false) String descricao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Conta> contas = contaService.buscarContasComFiltros(dataVencimento, descricao, pageable);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<Conta>> listarContasPorId(@PathVariable Long id, Pageable pageable) {
        Page<Conta> contas = contaService.buscarContasPorId(id, pageable);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/total-pago")
    public ResponseEntity<BigDecimal> obterValorTotalPagoPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        BigDecimal valorTotalPago = contaService.calcularValorTotalPagoPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(valorTotalPago);
    }

    @PostMapping("/importar")
    public ResponseEntity<List<Conta>> importarContas(@RequestParam("file") MultipartFile file) {
        try {
            List<Conta> contasImportadas = contaService.importarContas(file);
            return new ResponseEntity<>(contasImportadas, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
