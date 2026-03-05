package com.bancoDigital.carteira.controller;

import com.bancoDigital.carteira.request.AccountRequest;
import com.bancoDigital.carteira.request.AccountResponse;
import com.bancoDigital.carteira.request.DepositRequest;
import com.bancoDigital.carteira.request.WithdrawRequest;
import com.bancoDigital.carteira.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/conta")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping
    public ResponseEntity<AccountRequest> create(@RequestBody AccountRequest request) {
        request = service.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(request.getId()).toUri();
        return ResponseEntity.created(uri).body(request);
    }

    @GetMapping
    public ResponseEntity<List<AccountRequest>> findAll(){
        List <AccountRequest> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AccountRequest> findByid(@PathVariable String id){
        AccountRequest request = service.findById(id);
        return ResponseEntity.ok().body(request);
    }
    @PutMapping (value = "/{id}")
    public ResponseEntity<AccountRequest> update(@PathVariable String id, @RequestBody AccountRequest request) {
        request = service.update(id, request);
        return ResponseEntity.ok().body(request);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<AccountRequest> delete (@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deposito")
    public ResponseEntity<AccountRequest> addBalance(
            @PathVariable String id,
            @RequestBody @Valid DepositRequest depositRequest) {
        return ResponseEntity.ok(service.addBalance(id, depositRequest));
    }

    @PutMapping("/{id}/sacar")
    public ResponseEntity<AccountRequest> withdrawOperation(
            @PathVariable String id,
            @RequestBody @Valid WithdrawRequest withdrawRequest) {
        return ResponseEntity.ok(service.withdrawOperation(id, withdrawRequest));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<AccountResponse> getBalance(@PathVariable String id) {
        AccountResponse result = service.getBalance(id);
        return ResponseEntity.ok(result);
    }

}

