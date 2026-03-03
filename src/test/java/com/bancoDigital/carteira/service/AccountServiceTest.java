package com.bancoDigital.carteira.service;

import com.bancoDigital.carteira.domain.Account;
import com.bancoDigital.carteira.domain.Customer;
import com.bancoDigital.carteira.exception.DatabaseException;
import com.bancoDigital.carteira.exception.ResourceNotFoundException;
import com.bancoDigital.carteira.repository.AccountRepository;
import com.bancoDigital.carteira.repository.CustomerRepository;
import com.bancoDigital.carteira.request.AccountRequest;
import com.bancoDigital.carteira.request.DepositRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService service;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    private AccountRequest accountRequest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("João Silva");
        customer.setDocument("12345678900");

        accountRequest = new AccountRequest(
                1,
                "12345-6",
                "0001",
                BigDecimal.ZERO,
                LocalDateTime.now(),
                customer
        );
    }

    @Test
    void shouldCreateAccountSuccessfully() {

        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AccountRequest result = service.create(accountRequest);

        assertNotNull(result);
        assertEquals("12345-6", result.getAccountNumber());
        assertEquals(BigDecimal.ZERO, result.getBalance());

        verify(customerRepository).save(any(Customer.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldReturnAccounts() {
        Account account = new Account();
        account.setId(1);

        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<AccountRequest> result = service.findAll();

        assertEquals(1, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    void shouldReturnAccountWhenExists() {
        Account account = new Account();
        account.setId(1);

        when(accountRepository.findById("1")).thenReturn(Optional.of(account));

        AccountRequest result = service.findById("1");

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void shouldThrowWhenAccountNotFound() {
        when(accountRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById("1"));
    }


    @Test
    void shouldUpdateAccount() {
        Account entity = new Account();
        entity.setId(1);
        entity.setBalance(BigDecimal.TEN);

        AccountRequest request = new AccountRequest();
        request.setBalance(BigDecimal.valueOf(200));

        when(accountRepository.getReferenceById("1")).thenReturn(entity);
        when(accountRepository.save(any(Account.class))).thenReturn(entity);

        AccountRequest result = service.update("1", request);

        assertEquals(BigDecimal.valueOf(200), result.getBalance());
    }


    @Test
    void shouldThrowWhenDeleteNotFound() {
        doThrow(EmptyResultDataAccessException.class)
                .when(accountRepository).deleteById("1");

        assertThrows(ResourceNotFoundException.class,
                () -> service.delete("1"));
    }

    @Test
    void shouldThrowDatabaseExceptionOnDelete() {
        doThrow(DataIntegrityViolationException.class)
                .when(accountRepository).deleteById("1");

        assertThrows(DatabaseException.class,
                () -> service.delete("1"));
    }


    @Test
    void shouldAddBalance() {
        Account entity = new Account();
        entity.setId(1);
        entity.setBalance(BigDecimal.valueOf(100));

        DepositRequest deposit = new DepositRequest();
        deposit.setDeposit(BigDecimal.valueOf(50));

        when(accountRepository.getReferenceById("1")).thenReturn(entity);
        when(accountRepository.save(any(Account.class))).thenReturn(entity);

        AccountRequest result = service.addBalance("1", deposit);

        assertEquals(BigDecimal.valueOf(150), result.getBalance());
    }
}



