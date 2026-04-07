package com.bancoDigital.carteira.repository;

import com.bancoDigital.carteira.domain.BankStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, String> {
    Page<BankStatement> findAccountById(Long id, Pageable pageable);
}
