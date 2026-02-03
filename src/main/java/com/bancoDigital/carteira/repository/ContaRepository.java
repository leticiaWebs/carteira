package com.bancoDigital.carteira.repository;

import com.bancoDigital.carteira.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, String> {
}
