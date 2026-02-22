package com.bancoDigital.carteira.service;

import com.bancoDigital.carteira.domain.Cliente;
import com.bancoDigital.carteira.domain.Conta;
import com.bancoDigital.carteira.request.ContaRequest;
import com.bancoDigital.carteira.repository.ClienteRepository;
import com.bancoDigital.carteira.repository.ContaRepository;
import com.bancoDigital.carteira.exception.DatabaseException;
import com.bancoDigital.carteira.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @InjectMocks
    private ContaService service;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    private Conta conta;
    private Cliente cliente;
    private ContaRequest dto;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setNome("Leticia");
        cliente.setDocumento("12345678901");

        conta = new Conta();
        conta.setId(1);
        conta.setNumeroConta("123");
        conta.setNumeroAgencia("0001");
        conta.setSaldo("0");
        conta.setDataCriacao(LocalDateTime.now());
        conta.setCliente(cliente);

        dto = new ContaRequest(conta);
    }


    @Test
    void createShouldReturnContaDtoWhenSuccess() {
        when(clienteRepository.save(any())).thenReturn(cliente);
        when(contaRepository.save(any())).thenReturn(conta);

        ContaRequest result = service.create(dto);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(any());
        verify(contaRepository, times(1)).save(any());
    }


    @Test
    void findAllShouldReturnList() {
        when(contaRepository.findAll()).thenReturn(List.of(conta));

        List<ContaRequest> result = service.findAll();

        assertEquals(1, result.size());
        verify(contaRepository, times(1)).findAll();
    }


    @Test
    void findByIdShouldReturnContaDtoWhenExists() {
        when(contaRepository.findById("1")).thenReturn(Optional.of(conta));

        ContaRequest result = service.findById("1");

        assertNotNull(result);
        verify(contaRepository).findById("1");
    }

    @Test
    void findByIdShouldThrowWhenNotExists() {
        when(contaRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById("1"));
    }


    @Test
    void updateShouldReturnContaDtoWhenSuccess() {
        when(contaRepository.getReferenceById("1")).thenReturn(conta);
        when(contaRepository.save(any())).thenReturn(conta);

        ContaRequest result = service.update("1", dto);

        assertNotNull(result);
        verify(contaRepository).getReferenceById("1");
        verify(contaRepository).save(any());
    }

    @Test
    void updateShouldThrowWhenIdNotFound() {
        when(contaRepository.getReferenceById("1"))
                .thenThrow(EntityNotFoundException.class);
        assertThrows(ResourceNotFoundException.class,
                () -> service.update("1", dto));
    }


    @Test
    void deleteShouldDoNothingWhenSuccess() {
        doNothing().when(contaRepository).deleteById("1");

        service.delete("1");

        verify(contaRepository).deleteById("1");
    }

    @Test
    void deleteShouldThrowResourceNotFound() {
        doThrow(EmptyResultDataAccessException.class)
                .when(contaRepository).deleteById("1");

        assertThrows(ResourceNotFoundException.class,
                () -> service.delete("1"));
    }

    @Test
    void deleteShouldThrowDatabaseException() {
        doThrow(DataIntegrityViolationException.class)
                .when(contaRepository).deleteById("1");

        assertThrows(DatabaseException.class,
                () -> service.delete("1"));
    }
}
