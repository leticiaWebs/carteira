package com.bancoDigital.carteira.controller;

import com.bancoDigital.carteira.service.AccountService;
import com.seuproject.model.*;
import com.seuprojeto.api.ContaApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController implements ContaApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountResponse> createAccount(AccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.create(request));
    }
    @Override
    public ResponseEntity<AccountResponse> depositMoney(String id, Deposit deposit) {
        return ResponseEntity.ok(accountService.addBalance(id, deposit));
    }

    @Override
    public ResponseEntity<AccountResponse> withdrawMoney(String id, Withdraw withdraw) {
        return ResponseEntity.ok(accountService.withdrawOperation(id, withdraw));
    }

    @Override
    public ResponseEntity<List<AccountResponse>> findAllAccounts() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @Override
    public ResponseEntity<AccountResponse> findAccountById(String id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AccountResponse> getAccountBalance(String id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    @Override
    public ResponseEntity<List<BankStatementRepresentation>> getAccountStatement(
            String id, Integer page, Integer size) {
        return ResponseEntity.ok(accountService.getStatement(id, page, size));
    }
}
