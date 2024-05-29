package com.cbyk.contas_pagar_api.repository;


import com.cbyk.contas_pagar_api.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Page<Conta> findByDataVencimento(LocalDate dataVencimento, Pageable pageable);
    Page<Conta> findByDescricaoContaining(String descricao, Pageable pageable);
    Page<Conta> findByDataVencimentoAndDescricaoContaining(LocalDate dataVencimento, String descricao, Pageable pageable);
    Page<Conta> findById(Long id, Pageable pageable);
    List<Conta> findByDataPagamentoBetweenAndSituacao(LocalDate dataInicio, LocalDate dataFim, String paga);
}
