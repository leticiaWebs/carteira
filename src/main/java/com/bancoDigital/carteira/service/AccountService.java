package com.bancoDigital.carteira.service;

import com.bancoDigital.carteira.domain.Account;
import com.bancoDigital.carteira.domain.Customer;
import com.bancoDigital.carteira.exception.DadosInvalidosException;
import com.bancoDigital.carteira.exception.DatabaseException;
import com.bancoDigital.carteira.exception.ResourceNotFoundException;
import com.bancoDigital.carteira.mapper.AccountMapper;
import com.bancoDigital.carteira.repository.AccountRepository;
import com.bancoDigital.carteira.repository.CustomerRepository;
import com.bancoDigital.carteira.utils.AccountValidations;
import com.seuproject.model.AccountRequest;
import com.seuproject.model.AccountResponse;
import com.seuproject.model.Deposit;
import com.seuproject.model.Withdraw;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse create(AccountRequest request) {
        try {
            Account entity = accountMapper.toDomain(request);

            Customer customer = new Customer();
            customer.setName(request.getCustomer().getName());
            customer.setDocument(request.getCustomer().getDocument());
            customerRepository.save(customer);

            entity.setCustomer(customer);
            entity.setBalance(BigDecimal.ZERO);
            entity.setCreatedAt(LocalDateTime.now());

            return accountMapper.toResponse(accountRepository.save(entity));

        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(e.getMessage());
        }
    }

    @Transactional
    public List<AccountResponse> findAll() {
        //Lista e, fluxo de dados e processa casa item
        return accountRepository.findAll().stream()
                //converte casa item do domain em response
                .map(accountMapper::toResponse)
                //encerra o processamento e retorna uma lista
                .toList();
    }

    @Transactional
    public AccountResponse findById(String id) {
        Account entity = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
        return accountMapper.toResponse(entity);
    }

    @Transactional
    public AccountResponse update(String id, AccountRequest request) {
        try {
            Account entity = accountRepository.getReferenceById(id);
            entity.setAccountNumber(request.getAccountNumber());
            entity.setAgencyNumber(request.getAgencyNumber());
            return accountMapper.toResponse(accountRepository.save(entity));

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Account not found: " + id);
        }
    }

    @Transactional
    public void delete(String id) {
        try {
            accountRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Account not found" + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    @Transactional
    public AccountResponse addBalance(String id, Deposit depositRequest) {
        try {
            Account entity = accountRepository.getReferenceById(id);
            entity.setBalance(entity.getBalance().add(depositRequest.getAmount()));
            return accountMapper.toResponse(accountRepository.save(entity));

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Account not found: " + id);
        }
    }

    @Transactional
    public AccountResponse withdrawOperation(String id, Withdraw withdraw) {
        try {
            Account entity = accountRepository.getReferenceById(id);
            AccountValidations.verifyPositiveValues(withdraw.getAmount());
            AccountValidations.validateSufficientBalance(entity.getBalance(), withdraw.getAmount());

            entity.setBalance(entity.getBalance().subtract(withdraw.getAmount()));

            return accountMapper.toResponse(accountRepository.save(entity));

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Account not found: " + id);
        }
    }

    @Transactional
    public AccountResponse getBalance(String id) {
        try {
            Account entity = accountRepository.getReferenceById(id);
            return accountMapper.toResponse(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Account not found: " + id);
        }
    }

}


