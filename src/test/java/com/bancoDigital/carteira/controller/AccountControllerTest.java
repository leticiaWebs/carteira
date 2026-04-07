package com.bancoDigital.carteira.controller;

import com.bancoDigital.carteira.service.AccountService;
import com.seuproject.model.AccountRequest;
import com.seuproject.model.AccountResponse;
import com.seuproject.model.Customer;
import com.seuproject.model.Deposit;
import com.seuproject.model.Withdraw;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountResponse accountResponse;
    private AccountRequest accountRequest;
    private final String ACCOUNT_ID = "1";

    @BeforeEach
    void setUp() {
        accountResponse = new AccountResponse();
        accountResponse.setId(ACCOUNT_ID);
        accountResponse.setAccountNumber("123456");
        accountResponse.setAgencyNumber("0001");
        accountResponse.setBalance(1500.75);
        accountResponse.setCreatedAt(OffsetDateTime.now());

        Customer customer = new Customer();
        customer.setDocument("123.456.789-00");
        customer.setName("Maria Silva");

        accountRequest = new AccountRequest();
        accountRequest.setAccountNumber("123456");
        accountRequest.setAgencyNumber("0001");
        accountRequest.setCustomer(customer);
    }

    @Nested
    class CreateAccount {

        void shouldReturn201WithCreatedAccount() {
            when(accountService.create(any(AccountRequest.class))).thenReturn(accountResponse);

            ResponseEntity<AccountResponse> response = accountController.createAccount(accountRequest);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(ACCOUNT_ID);
            assertThat(response.getBody().getAccountNumber()).isEqualTo("123456");
            assertThat(response.getBody().getAgencyNumber()).isEqualTo("0001");
            assertThat(response.getBody().getBalance()).isEqualTo(1500.75);
            verify(accountService, times(1)).create(accountRequest);
        }

        @Test
        void shouldPropagateExceptionFromService() {
            when(accountService.create(any(AccountRequest.class)))
                    .thenThrow(new RuntimeException("Erro ao criar conta"));

            assertThatThrownBy(() -> accountController.createAccount(accountRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro ao criar conta");
        }

        @Test
        @DisplayName("deve chamar accountService.create com o request correto")
        void shouldDelegateToServiceWithCorrectRequest() {
            when(accountService.create(accountRequest)).thenReturn(accountResponse);

            accountController.createAccount(accountRequest);

            verify(accountService).create(accountRequest);
            verifyNoMoreInteractions(accountService);
        }
    }

    @Nested
    class DepositMoney {

        private Deposit deposit;

        @BeforeEach
        void setUp() {
            deposit = new Deposit();
            deposit.setAmount(new java.math.BigDecimal("500.00"));
        }

        @Test
        void shouldReturn200WithUpdatedBalanceAfterDeposit() {
            accountResponse.setBalance(2000.75);
            when(accountService.addBalance(eq(ACCOUNT_ID), any(Deposit.class))).thenReturn(accountResponse);

            ResponseEntity<AccountResponse> response = accountController.depositMoney(ACCOUNT_ID, deposit);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getBalance()).isEqualTo(2000.75);
            verify(accountService, times(1)).addBalance(ACCOUNT_ID, deposit);
        }

        @Test
        void shouldPropagateExceptionWhenAccountNotFound() {
            when(accountService.addBalance(eq(ACCOUNT_ID), any(Deposit.class)))
                    .thenThrow(new RuntimeException("Conta não encontrada"));

            assertThatThrownBy(() -> accountController.depositMoney(ACCOUNT_ID, deposit))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Conta não encontrada");
        }

        @Test
        void shouldPassCorrectIdAndDepositToService() {
            when(accountService.addBalance(ACCOUNT_ID, deposit)).thenReturn(accountResponse);

            accountController.depositMoney(ACCOUNT_ID, deposit);

            verify(accountService).addBalance(ACCOUNT_ID, deposit);
            verifyNoMoreInteractions(accountService);
        }
    }


    @Nested
    class WithdrawMoney {

        private Withdraw withdraw;

        @BeforeEach
        void setUp() {
            withdraw = new Withdraw();
            withdraw.setAmount(new java.math.BigDecimal("300.00"));
        }

        @Test
        void shouldReturn200WithUpdatedBalanceAfterWithdraw() {
            accountResponse.setBalance(1200.75);
            when(accountService.withdrawOperation(eq(ACCOUNT_ID), any(Withdraw.class))).thenReturn(accountResponse);

            ResponseEntity<AccountResponse> response = accountController.withdrawMoney(ACCOUNT_ID, withdraw);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getBalance()).isEqualTo(1200.75);
            verify(accountService, times(1)).withdrawOperation(ACCOUNT_ID, withdraw);
        }

        @Test
        void shouldPropagateExceptionWhenInsufficientBalance() {
            when(accountService.withdrawOperation(eq(ACCOUNT_ID), any(Withdraw.class)))
                    .thenThrow(new RuntimeException("Saldo insuficiente"));

            assertThatThrownBy(() -> accountController.withdrawMoney(ACCOUNT_ID, withdraw))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Saldo insuficiente");
        }

        @Test
        void shouldPassCorrectIdAndWithdrawToService() {
            when(accountService.withdrawOperation(ACCOUNT_ID, withdraw)).thenReturn(accountResponse);

            accountController.withdrawMoney(ACCOUNT_ID, withdraw);

            verify(accountService).withdrawOperation(ACCOUNT_ID, withdraw);
            verifyNoMoreInteractions(accountService);
        }
    }

    @Nested
    @DisplayName("findAllAccounts")
    class FindAllAccounts {

        @Test
        void shouldReturn200WithListOfAccounts() {
            AccountResponse second = new AccountResponse();
            second.setId("2");
            second.setAccountNumber("654321");
            List<AccountResponse> accounts = List.of(accountResponse, second);
            when(accountService.findAll()).thenReturn(accounts);

            ResponseEntity<List<AccountResponse>> response = accountController.findAllAccounts();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(2);
            assertThat(response.getBody().get(0).getId()).isEqualTo("1");
            assertThat(response.getBody().get(1).getId()).isEqualTo("2");
            verify(accountService, times(1)).findAll();
        }

        @Test
        void shouldReturn200WithEmptyList() {
            when(accountService.findAll()).thenReturn(Collections.emptyList());

            ResponseEntity<List<AccountResponse>> response = accountController.findAllAccounts();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("deve propagar exceção do service")
        void shouldPropagateExceptionFromService() {
            when(accountService.findAll()).thenThrow(new RuntimeException("Erro interno"));

            assertThatThrownBy(() -> accountController.findAllAccounts())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Erro interno");
        }
    }


    @Nested
    @DisplayName("findAccountById")
    class FindAccountById {

        @Test
        void shouldReturn200WithFoundAccount() {
            when(accountService.findById(ACCOUNT_ID)).thenReturn(accountResponse);

            ResponseEntity<AccountResponse> response = accountController.findAccountById(ACCOUNT_ID);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(ACCOUNT_ID);
            assertThat(response.getBody().getAccountNumber()).isEqualTo("123456");
            verify(accountService, times(1)).findById(ACCOUNT_ID);
        }

        @Test
        void shouldPropagateExceptionWhenAccountDoesNotExist() {
            when(accountService.findById(ACCOUNT_ID))
                    .thenThrow(new RuntimeException("Conta não encontrada"));

            assertThatThrownBy(() -> accountController.findAccountById(ACCOUNT_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Conta não encontrada");
        }

        @Test
        void shouldPassCorrectIdToService() {
            when(accountService.findById(ACCOUNT_ID)).thenReturn(accountResponse);

            accountController.findAccountById(ACCOUNT_ID);

            verify(accountService).findById(ACCOUNT_ID);
            verifyNoMoreInteractions(accountService);
        }
    }


    @Nested
    @DisplayName("deleteAccount")
    class DeleteAccount {

        @Test
        void shouldReturn204AfterDeletion() {
            doNothing().when(accountService).delete(ACCOUNT_ID);

            ResponseEntity<Void> response = accountController.deleteAccount(ACCOUNT_ID);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();
            verify(accountService, times(1)).delete(ACCOUNT_ID);
        }

        @Test
        void shouldPropagateExceptionWhenAccountDoesNotExist() {
            doThrow(new RuntimeException("Conta não encontrada")).when(accountService).delete(ACCOUNT_ID);

            assertThatThrownBy(() -> accountController.deleteAccount(ACCOUNT_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Conta não encontrada");
        }

        @Test
        void shouldPassCorrectIdToService() {
            doNothing().when(accountService).delete(ACCOUNT_ID);

            accountController.deleteAccount(ACCOUNT_ID);

            verify(accountService).delete(ACCOUNT_ID);
            verifyNoMoreInteractions(accountService);
        }
    }


    @Nested
    @DisplayName("getAccountBalance")
    class GetAccountBalance {

        @Test
        void shouldReturn200WithAccountBalance() {
            when(accountService.getBalance(ACCOUNT_ID)).thenReturn(accountResponse);

            ResponseEntity<AccountResponse> response = accountController.getAccountBalance(ACCOUNT_ID);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getBalance()).isEqualTo(1500.75);
            verify(accountService, times(1)).getBalance(ACCOUNT_ID);
        }

        @Test
        void shouldReturn200WithZeroBalance() {
            accountResponse.setBalance(0.0);
            when(accountService.getBalance(ACCOUNT_ID)).thenReturn(accountResponse);

            ResponseEntity<AccountResponse> response = accountController.getAccountBalance(ACCOUNT_ID);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getBalance()).isEqualTo(0.0);
        }

        @Test
        void shouldPropagateExceptionWhenAccountDoesNotExist() {
            when(accountService.getBalance(ACCOUNT_ID))
                    .thenThrow(new RuntimeException("Conta não encontrada"));

            assertThatThrownBy(() -> accountController.getAccountBalance(ACCOUNT_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Conta não encontrada");
        }

        @Test
        void shouldPassCorrectIdToService() {
            when(accountService.getBalance(ACCOUNT_ID)).thenReturn(accountResponse);

            accountController.getAccountBalance(ACCOUNT_ID);

            verify(accountService).getBalance(ACCOUNT_ID);
            verifyNoMoreInteractions(accountService);
        }
    }
}