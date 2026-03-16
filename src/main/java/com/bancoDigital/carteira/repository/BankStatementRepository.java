package com.bancoDigital.carteira.repository;

import com.bancoDigital.carteira.domain.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
}
