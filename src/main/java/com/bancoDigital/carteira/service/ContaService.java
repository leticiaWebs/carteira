package com.bancoDigital.carteira.service;

import com.bancoDigital.carteira.domain.Cliente;
import com.bancoDigital.carteira.domain.Conta;
import com.bancoDigital.carteira.dto.ClienteDto;
import com.bancoDigital.carteira.dto.ContaDto;
import com.bancoDigital.carteira.repository.ClienteRepository;
import com.bancoDigital.carteira.repository.ContaRepository;
import com.bancoDigital.carteira.service.exceptions.DatabaseException;
import com.bancoDigital.carteira.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public ContaDto create(ContaDto dto) {
        Conta entity = new Conta();
        entity.setId(dto.getId());
        entity.setSaldo(dto.getSaldo());
        entity.setNumeroAgencia(dto.getNumeroAgencia());
        entity.setNumeroConta(dto.getNumeroConta());
        entity.setDataCriacao(dto.getDataCriacao());

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getCliente().getNome());
        cliente.setDocumento(dto.getCliente().getDocumento());

        clienteRepository.save(cliente);
        entity.setCliente(cliente);
        contaRepository.save(entity);
        return new ContaDto(entity);

    }

    @Transactional
    public List<ContaDto> findAll() {
        List<Conta> list = contaRepository.findAll();
        return list.stream().map(ContaDto::new).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public ContaDto findById(String id) {
        Optional<Conta> obj = contaRepository.findById(id);
        Conta entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ContaDto(entity);
    }

    @Transactional
    public ContaDto update(String id, ContaDto dto) {
        try {
            Conta entity = contaRepository.getReferenceById(id);
            entity.setSaldo(dto.getSaldo());
            entity = contaRepository.save(entity);
            return new ContaDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Account number not found" + id);
        }
    }

    public void delete(String id) {
        try {
            contaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Usuario não encontrado");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void validarDocumento(String documento){
        if(documento == null || documento.matches("\\d{11}")){
            throw new IllegalArgumentException("CPF inválido.");
        }
    }

    private void validaNomeTitular(String nome){
        if(nome == null || nome.isBlank()){
            throw new IllegalArgumentException("Nome está vazio ou é invalido");
        }
    }



}
