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
import com.seuproject.model.AccountRequest;
import com.seuproject.model.AccountResponse;
import com.seuproject.model.Deposit;
import com.seuproject.model.Withdraw;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private BankStatementRepository bankStatementRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private AccountResponse accountResponse;
    private AccountRequest accountRequest;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setName("Tobias Bolo");
        customer.setDocument("13754629619");

        account = new Account();
        account.setId(1);
        account.setAccountNumber("12345");
        account.setAgencyNumber("001");
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCreatedAt(OffsetDateTime.now());
        account.setCustomer(customer);

        accountResponse = new AccountResponse();
        accountResponse.setId("1");
        accountResponse.setAccountNumber("12345");
        accountResponse.setAgencyNumber("001");

        accountRequest = new AccountRequest();
        accountRequest.setAccountNumber("12345");
        accountRequest.setAgencyNumber("001");
    }

    @Test
    void createShouldThrowDadosInvalidosException() {
        when(accountMapper.toDomain(accountRequest)).thenThrow(new IllegalArgumentException("Invalid data"));

        assertThatThrownBy(() -> accountService.create(accountRequest))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("Invalid data");
    }


    @Test
    void findAllShouldReturnListOfAccountResponses() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        List<AccountResponse> result = accountService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAccountNumber()).isEqualTo("12345");
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of());

        List<AccountResponse> result = accountService.findAll();

        assertThat(result).isEmpty();
    }


    @Test
    void findByIdShouldReturnAccountResponseWhenFound() {
        when(accountRepository.findById("1")).thenReturn(Optional.of(account));
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse response = accountService.findById("1");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("1");
        verify(accountRepository, times(1)).findById("1");
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenNotFound() {
        when(accountRepository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findById("99"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Account not found: 99");
    }


    @Test
    void updateShouldReturnUpdatedAccountWhenFound() {
        when(accountRepository.getReferenceById("1")).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse response = accountService.update("1", accountRequest);

        assertThat(response).isNotNull();
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenEntityNotFound() {
        when(accountRepository.getReferenceById("99")).thenThrow(new EntityNotFoundException());

        assertThatThrownBy(() -> accountService.update("99", accountRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Account not found: 99");
    }

    @Test
    void deleteShouldDeleteAccountWhenFound() {
        doNothing().when(accountRepository).deleteById("1");

        accountService.delete("1");

        verify(accountRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenEmptyResult() {
        doThrow(new EmptyResultDataAccessException(1)).when(accountRepository).deleteById("99");

        assertThatThrownBy(() -> accountService.delete("99"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Account not found");
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenIntegrityViolation() {
        doThrow(new DataIntegrityViolationException("")).when(accountRepository).deleteById("1");

        assertThatThrownBy(() -> accountService.delete("1"))
                .isInstanceOf(DatabaseException.class)
                .hasMessageContaining("Integrity violation");
    }


    @Test
    void getBalance_ShouldReturnAccountResponse_WhenFound() {
        when(accountRepository.getReferenceById("1")).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse response = accountService.getBalance("1");

        assertThat(response).isNotNull();
        verify(accountRepository, times(1)).getReferenceById("1");
    }

    @Test
    void getBalance_ShouldThrowResourceNotFoundException_WhenEntityNotFound() {
        when(accountRepository.getReferenceById("99")).thenThrow(new EntityNotFoundException());

        assertThatThrownBy(() -> accountService.getBalance("99"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Account not found: 99");
    }

    @Test
    void addBalance_ShouldDepositAndSaveBankStatement() {
        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(500));

        when(accountRepository.findById("1")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse response = accountService.addBalance("1", deposit);

        assertThat(response).isNotNull();
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500));
        verify(accountRepository, times(1)).save(account);
        verify(bankStatementRepository, times(1)).save(any(BankStatement.class));
    }

    @Test
    void addBalance_ShouldThrowRuntimeException_WhenAccountNotFound() {
        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(500));

        when(accountRepository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.addBalance("99", deposit))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account not found");
    }

    @Test
    void withdrawOperation_ShouldWithdrawAndSaveBankStatement() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(BigDecimal.valueOf(200));

        when(accountRepository.findById("1")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        AccountResponse response = accountService.withdrawOperation("1", withdraw);

        assertThat(response).isNotNull();
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(800));
        verify(accountRepository, times(1)).save(account);
        verify(bankStatementRepository, times(1)).save(any(BankStatement.class));
    }

    @Test
    void withdrawOperation_ShouldThrowRuntimeException_WhenInsufficientBalance() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(BigDecimal.valueOf(9999));

        when(accountRepository.findById("1")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> accountService.withdrawOperation("1", withdraw))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void withdrawOperation_ShouldThrowRuntimeException_WhenAccountNotFound() {
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(BigDecimal.valueOf(200));

        when(accountRepository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.withdrawOperation("99", withdraw))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account not found");
    }

    @Test
    void addBalance_ShouldSaveBankStatementWithCorrectFields() {
        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(300));

        when(accountRepository.findById("1")).thenReturn(Optional.of(account));
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        accountService.addBalance("1", deposit);

        ArgumentCaptor<BankStatement> captor = ArgumentCaptor.forClass(BankStatement.class);
        verify(bankStatementRepository).save(captor.capture());

        BankStatement saved = captor.getValue();
        assertThat(saved.getOperationType()).isEqualTo("DEPOSIT");
        assertThat(saved.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(300));
        assertThat(saved.getOperationDescription()).isEqualTo("Deposit has been made");
        assertThat(saved.getAccount()).isEqualTo(account);
    }

}



