package com.bancoDigital.carteira.controller;

import com.bancoDigital.carteira.domain.Account;
import com.bancoDigital.carteira.service.AccountService;
import com.seuproject.model.AccountRequest;
import com.seuproject.model.AccountResponse;
import com.seuproject.model.Deposit;
import com.seuproject.model.Withdraw;
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
}
