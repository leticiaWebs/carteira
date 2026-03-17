package com.bancoDigital.carteira.repository;

import com.bancoDigital.carteira.domain.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, String> {
    List<BankStatement> findAccountById(String id);
}
