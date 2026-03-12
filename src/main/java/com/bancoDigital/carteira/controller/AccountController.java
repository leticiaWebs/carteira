package com.bancoDigital.carteira.controller;

import com.bancoDigital.carteira.service.AccountService;
import com.seuproject.model.AccountRequest;
import com.seuproject.model.Deposit;

import com.seuproject.model.AccountResponse;
import com.seuprojeto.api.ContaApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController implements ContaApi {

    private AccountService accountService;

    @Override
    public ResponseEntity<AccountResponse> createAccount(AccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.create(request));
    }

//    @GetMapping
//    public ResponseEntity<List<AccountRequest>> findAll(){
//        List <AccountRequest> list = service.findAll();
//        return ResponseEntity.ok().body(list);
//    }
//
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<AccountRequest> findByid(@PathVariable String id){
//        AccountRequest request = service.findById(id);
//        return ResponseEntity.ok().body(request);
//    }
//    @PutMapping (value = "/{id}")
//    public ResponseEntity<AccountRequest> update(@PathVariable String id, @RequestBody AccountRequest request) {
//        request = service.update(id, request);
//        return ResponseEntity.ok().body(request);
//    }
//
//    @DeleteMapping (value = "/{id}")
//    public ResponseEntity<AccountRequest> delete (@PathVariable String id) {
//        service.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/{id}/deposito")
//    public ResponseEntity<AccountRequest> addBalance(
//            @PathVariable String id,
//            @RequestBody @Valid DepositRequest depositRequest) {
//        return ResponseEntity.ok(service.addBalance(id, depositRequest));
//    }
//
//    @PutMapping("/{id}/sacar")
//    public ResponseEntity<AccountRequest> withdrawOperation(
//            @PathVariable String id,
//            @RequestBody @Valid WithdrawRequest withdrawRequest) {
//        return ResponseEntity.ok(service.withdrawOperation(id, withdrawRequest));
//    }
//
//    @GetMapping("/{id}/balance")
//    public ResponseEntity<AccountResponse> getBalance(@PathVariable String id) {
//        AccountResponse result = service.getBalance(id);
//        return ResponseEntity.ok(result);
//    }

}

