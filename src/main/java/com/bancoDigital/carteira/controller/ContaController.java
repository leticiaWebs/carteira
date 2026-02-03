package com.bancoDigital.carteira.controller;


import com.bancoDigital.carteira.dto.ContaDto;
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
    public ResponseEntity<ContaDto> create(@RequestBody ContaDto dto) {
        dto = service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<ContaDto>> findAll(){
        List <ContaDto> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContaDto> findByid(@PathVariable String id){
        ContaDto dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }
    @PutMapping (value = "/{id}")
    public ResponseEntity<ContaDto> insert (@PathVariable String id, @RequestBody ContaDto dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping (value = "/{id}")
    public ResponseEntity<ContaDto> delete (@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
