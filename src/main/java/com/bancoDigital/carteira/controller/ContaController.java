package com.bancoDigital.carteira.controller;

import com.bancoDigital.carteira.request.ContaRequest;
import com.bancoDigital.carteira.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/conta")
public class ContaController {

    @Autowired
    private ContaService service;

    @PostMapping
    public ResponseEntity<ContaRequest> create(@RequestBody ContaRequest request) {
        request = service.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(request.getId()).toUri();
        return ResponseEntity.created(uri).body(request);
    }

    @GetMapping
    public ResponseEntity<List<ContaRequest>> findAll(){
        List <ContaRequest> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContaRequest> findByid(@PathVariable String id){
        ContaRequest request = service.findById(id);
        return ResponseEntity.ok().body(request);
    }
    @PutMapping (value = "/{id}")
    public ResponseEntity<ContaRequest> update(@PathVariable String id, @RequestBody ContaRequest request) {
        request = service.update(id, request);
        return ResponseEntity.ok().body(request);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<ContaRequest> delete (@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping (value = "/contas/{id}/depositar")
    public ResponseEntity<ContaRequest> deposito(@PathVariable String id, @RequestBody ContaRequest dto){
        dto = service.deposito(id, dto);
        return ResponseEntity.ok().body(dto);
    }

}
