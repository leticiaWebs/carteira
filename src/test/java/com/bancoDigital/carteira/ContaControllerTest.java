package com.bancoDigital.carteira;

import com.bancoDigital.carteira.controller.ContaController;
import com.bancoDigital.carteira.dto.ContaDto;
import com.bancoDigital.carteira.repository.ContaRepository;
import com.bancoDigital.carteira.service.ContaService;
import generic.fixture.GenericFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContaControllerTest {

    @InjectMocks
    private ContaController contaController;

    @Mock
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;


    @Test
    void shouldCreateAccount(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        request.setRequestURI("/conta");

        ContaDto dto = new ContaDto();
        dto.setId(1);
        dto.setDataCriacao(LocalDateTime.parse("2026-02-03T11:49:17.6739914"));
        dto.setTitular("Tobias Bolo");
        dto.setNumeroAgencia("8367394");
        dto.setNumeroAgencia("38474");
        dto.setSaldo("1000");
        when(contaService.create(any(ContaDto.class))).thenReturn(dto);

        ResponseEntity<ContaDto> response = contaController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("/conta/1", response.getHeaders().getLocation().toString());
        assertEquals(dto, response.getBody());



    }

}
