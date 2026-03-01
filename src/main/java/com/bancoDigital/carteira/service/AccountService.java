package com.bancoDigital.carteira.service;

import com.bancoDigital.carteira.domain.Customer;
import com.bancoDigital.carteira.domain.Account;
import com.bancoDigital.carteira.request.AccountRequest;
import com.bancoDigital.carteira.repository.CustomerRepository;
import com.bancoDigital.carteira.repository.AccountRepository;
import com.bancoDigital.carteira.exception.DadosInvalidosException;
import com.bancoDigital.carteira.exception.DatabaseException;
import com.bancoDigital.carteira.exception.ResourceNotFoundException;
import com.bancoDigital.carteira.request.DepositRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private DepositRequest depositRequest;

    @Transactional
    public AccountRequest create(AccountRequest request) {
        try {
            Account entity = new Account();
            entity.setId(request.getId());
            entity.setBalance(request.getBalance());
            entity.setAgencyNumber(request.getAgencyNumber());
            entity.setAccountNumber(request.getAccountNumber());
            entity.setCreatedAt(request.getCreatedAt());

            Customer customer = new Customer();

            customer.setName(request.getCustomer().getName());
            customer.setDocument(request.getCustomer().getDocument());

            customerRepository.save(customer);
            entity.setCustomer(customer);
            accountRepository.save(entity);
            return new AccountRequest(entity);

        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(e.getMessage());
        }

    }

    @Transactional
    public List<AccountRequest> findAll() {
        List<Account> list = accountRepository.findAll();
        return list.stream().map(AccountRequest::new).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public AccountRequest findById(String id) {
        Optional<Account> obj = accountRepository.findById(id);
        Account entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new AccountRequest(entity);
    }

    @Transactional
    public AccountRequest update(String id, AccountRequest request) {
        try {
            Account entity = accountRepository.getReferenceById(id);
            entity.setBalance(request.getBalance());
//            entity.getCliente(request.getCliente().setNome());
            entity = accountRepository.save(entity);
            return new AccountRequest(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Account number not found" + id);
        }
    }

    @Transactional
    public void delete(String id) {
        try {
            accountRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Usuario não encontrado");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    @Transactional
    public AccountRequest addBalance(String id, DepositRequest depositRequest) {
        try {
            Account entity = accountRepository.getReferenceById(id);

            BigDecimal novoSaldo = entity.getBalance().add(depositRequest.getDeposit());
            entity.setBalance(novoSaldo);

            entity = accountRepository.save(entity);
            return new AccountRequest(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Conta não encontrada: " + id);
        }
    }
}


