package com.bancoDigital.carteira.service;

import com.bancoDigital.carteira.domain.Cliente;
import com.bancoDigital.carteira.domain.Conta;
import com.bancoDigital.carteira.request.ContaRequest;
import com.bancoDigital.carteira.repository.ClienteRepository;
import com.bancoDigital.carteira.repository.ContaRepository;
import com.bancoDigital.carteira.exception.DadosInvalidosException;
import com.bancoDigital.carteira.exception.DatabaseException;
import com.bancoDigital.carteira.exception.ResourceNotFoundException;
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
    public ContaRequest create(ContaRequest request) {
        try {
            Conta entity = new Conta();
            entity.setId(request.getId());
            entity.setSaldo(request.getSaldo());
            entity.setNumeroAgencia(request.getNumeroAgencia());
            entity.setNumeroConta(request.getNumeroConta());
            entity.setDataCriacao(request.getDataCriacao());

            Cliente cliente = new Cliente();

            cliente.setNome(request.getCliente().getNome());
            cliente.setDocumento(request.getCliente().getDocumento());

            clienteRepository.save(cliente);
            entity.setCliente(cliente);
            contaRepository.save(entity);
            return new ContaRequest(entity);

        } catch (IllegalArgumentException e) {
            throw new DadosInvalidosException(e.getMessage());
        }

    }

    @Transactional
    public List<ContaRequest> findAll() {
        List<Conta> list = contaRepository.findAll();
        return list.stream().map(ContaRequest::new).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public ContaRequest findById(String id) {
        Optional<Conta> obj = contaRepository.findById(id);
        Conta entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ContaRequest(entity);
    }

    @Transactional
    public ContaRequest update(String id, ContaRequest request) {
        try {
            Conta entity = contaRepository.getReferenceById(id);
            entity.setSaldo(request.getSaldo());
//            entity.getCliente(request.getCliente().setNome());
            entity = contaRepository.save(entity);
            return new ContaRequest(entity);
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

    public ContaRequest deposito(String id, ContaRequest dto) {
        try {
            Conta entity = contaRepository.getReferenceById(id);

            if (entity.getSaldo() <= 0) {
                throw new ResourceNotFoundException("Valor do desposito é invalido");
            }
            entity = contaRepository.save(entity);
            return new ContaRequest(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Account number not found" + id);
        }
    }
}


