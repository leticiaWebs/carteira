package com.bancoDigital.carteira.service;

import com.bancoDigital.carteira.domain.Account;
import com.bancoDigital.carteira.domain.BankStatement;
import com.bancoDigital.carteira.domain.Customer;
import com.bancoDigital.carteira.exception.DadosInvalidosException;
import com.bancoDigital.carteira.exception.DatabaseException;
import com.bancoDigital.carteira.exception.ResourceNotFoundException;
import com.bancoDigital.carteira.mapper.AccountMapper;
import com.bancoDigital.carteira.repository.AccountRepository;
import com.bancoDigital.carteira.repository.BankStatementRepository;
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
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountMapper accountMapper;
    private final BankStatementRepository bankStatementRepository;

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
            entity.setCreatedAt(OffsetDateTime.now());

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

    public AccountResponse deposit(String accountId, Deposit deposit) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));


        account.setBalance(account.getBalance().add(deposit.getAmount()));
        accountRepository.save(account);


        saveBankStatement("DEPOSIT", deposit.getAmount(), "Deposit has been made", account);

        return accountMapper.toResponse(account);
    }

    public AccountResponse withdraw(String accountId, Withdraw withdraw) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(withdraw.getAmount()) < 0)
            throw new RuntimeException("Insufficient balance");

        account.setBalance(account.getBalance().subtract(withdraw.getAmount()));
        accountRepository.save(account);

        saveBankStatement("WITHDRAWAL", withdraw.getAmount(), "withdrawal", account);

        return accountMapper.toResponse(account);
    }

    private void saveBankStatement(String type, BigDecimal value, String description, Account account) {
        BankStatement statement = new BankStatement();
        statement.setOperationType(type);
        statement.setValue(value);
        statement.setDateTime(OffsetDateTime.now());
        statement.setOperationDescription(description);
        statement.setAccount(account);
        bankStatementRepository.save(statement);
    }
}


