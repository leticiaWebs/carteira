package com.bancoDigital.carteira.controller;

import com.bancoDigital.carteira.request.AccountRequest;
import com.bancoDigital.carteira.request.DepositRequest;
import com.bancoDigital.carteira.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Testes do AccountController")
public class AccountControllerTest {

    @Mock
    private AccountService service;

    @InjectMocks
    private AccountController controller;

    private AccountRequest accountRequest;
    private DepositRequest depositRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        accountRequest = new AccountRequest();
        accountRequest.setId(1);
        accountRequest.setAccountNumber("123456");
        accountRequest.setAgencyNumber("0001");
        accountRequest.setBalance(new BigDecimal("1000.00"));

        depositRequest = new DepositRequest();
        depositRequest.setDeposit(new BigDecimal("500.00"));
    }

    @Test
    public void testCreate_Success() {
        when(service.create(any(AccountRequest.class))).thenReturn(accountRequest);

        ResponseEntity<AccountRequest> response = controller.create(accountRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        verify(service, times(1)).create(any(AccountRequest.class));
    }

    @Test
    public void testFindAll_Success() {
        List<AccountRequest> contas = Arrays.asList(
                accountRequest,
                new AccountRequest() {{
                    setId(2);
                    setAccountNumber("654321");
                    setAgencyNumber("0002");
                    setBalance(new BigDecimal("2000.00"));
                }}
        );
        when(service.findAll()).thenReturn(contas);

        ResponseEntity<List<AccountRequest>> response = controller.findAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).findAll();
    }

    @Test
    public void testFindAll_EmptyList() {
        when(service.findAll()).thenReturn(Arrays.asList());

        ResponseEntity<List<AccountRequest>> response = controller.findAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    @DisplayName("Deve buscar uma conta por ID com sucesso")
    public void testFindById_Success() {
        when(service.findById(String.valueOf(1))).thenReturn(accountRequest);

        ResponseEntity<AccountRequest> response = controller.findByid(String.valueOf(1));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        verify(service, times(1)).findById(String.valueOf(1));
    }

    @Test
    public void testFindById_NotFound() {
        when(service.findById(String.valueOf(999))).thenReturn(null);

        ResponseEntity<AccountRequest> response = controller.findByid(String.valueOf(999));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Deve atualizar uma conta com sucesso")
    public void testUpdate_Success() {
        AccountRequest contaAtualizada = new AccountRequest();
        contaAtualizada.setId(1);
        contaAtualizada.setAccountNumber("999999");
        contaAtualizada.setAgencyNumber("0001");
        contaAtualizada.setBalance(new BigDecimal("5000.00"));

        when(service.update(String.valueOf(eq(1)), any(AccountRequest.class))).thenReturn(contaAtualizada);

        ResponseEntity<AccountRequest> response = controller.update(String.valueOf(1), accountRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("999999", response.getBody().getAccountNumber());
        assertEquals(new BigDecimal("5000.00"), response.getBody().getBalance());
        verify(service, times(1)).update(String.valueOf(eq(1)), any(AccountRequest.class));
    }

    @Test
    @DisplayName("Deve deletar uma conta com sucesso")
    public void testDelete_Success() {
        doNothing().when(service).delete(String.valueOf(1));

        ResponseEntity<AccountRequest> response = controller.delete(String.valueOf(1));

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).delete(String.valueOf(1));
    }

    @Test
    @DisplayName("Deve adicionar saldo à conta com sucesso")
    public void testAddBalance_Success() {
        AccountRequest contaComSaldo = new AccountRequest();
        contaComSaldo.setId(1);
        contaComSaldo.setAccountNumber("123456");
        contaComSaldo.setAgencyNumber("0001");
        contaComSaldo.setBalance(new BigDecimal("1500.00"));

        when(service.addBalance(String.valueOf(eq(1)), any(DepositRequest.class))).thenReturn(contaComSaldo);

        ResponseEntity<AccountRequest> response = controller.addBalance(String.valueOf(1), depositRequest);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(new BigDecimal("1500.00"), response.getBody().getBalance());
        verify(service, times(1)).addBalance(String.valueOf(eq(1)), any(DepositRequest.class));
    }

//    @Test
//    public void testSubtractBalance_Success() {
//        AccountRequest contaComSaldo = new AccountRequest();
//        contaComSaldo.setId(1);
//        contaComSaldo.setAccountNumber("123456");
//        contaComSaldo.setAgencyNumber("0001");
//        contaComSaldo.setBalance(new BigDecimal("1500.00"));
//
//        when(service.withdrawOperation(String.valueOf(eq("150"))).thenReturn(contaComSaldo);
//
//    }


}