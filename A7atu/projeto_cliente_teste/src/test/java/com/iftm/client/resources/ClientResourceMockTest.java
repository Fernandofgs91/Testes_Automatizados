package com.iftm.client.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftm.client.dto.ClientDTO;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientResource.class)
public class ClientResourceMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Mock - Deve retornar um cliente com sucesso")
    public void findById_DeveRetornarClienteMockado() throws Exception {
        Long id = 1L;
        ClientDTO dto = new ClientDTO(id, "Cliente Mockado", "11122233344", 3000.0, Instant.parse("2000-01-01T00:00:00Z"), 1);

        Mockito.when(service.findById(id)).thenReturn(dto);

        mockMvc.perform(get("/clients/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value("Cliente Mockado"))
            .andExpect(jsonPath("$.cpf").value("11122233344"));
    }

    @Test
    @DisplayName("Mock - Deve criar um cliente e retornar os dados criados")
    public void insert_DeveCriarClienteComSucesso() throws Exception {
        ClientDTO dto = new ClientDTO(null, "Cliente Novo", "99999999999", 4000.0, Instant.parse("1990-01-01T00:00:00Z"), 2);
        ClientDTO created = new ClientDTO(100L, dto.getName(), dto.getCpf(), dto.getIncome(), dto.getBirthDate(), dto.getChildren());

        Mockito.when(service.insert(any())).thenReturn(created);

        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/clients/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.name").value("Cliente Novo"));
    }

    @Test
    @DisplayName("Mock - Deve atualizar cliente com sucesso")
    public void update_DeveAtualizarClienteComSucesso() throws Exception {
        Long id = 3L;
        ClientDTO dto = new ClientDTO(null, "Atualizado", "12345678900", 5000.0, Instant.parse("1980-01-01T00:00:00Z"), 3);
        ClientDTO updated = new ClientDTO(id, dto.getName(), dto.getCpf(), dto.getIncome(), dto.getBirthDate(), dto.getChildren());

        Mockito.when(service.update(eq(id), any())).thenReturn(updated);

        String jsonBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/clients/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value("Atualizado"))
            .andExpect(jsonPath("$.income").value(5000.0));
    }

    @Test
    @DisplayName("Mock - Deve deletar cliente com sucesso")
    public void delete_DeveRemoverClienteComSucesso() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(service).delete(id);

        mockMvc.perform(delete("/clients/{id}", id))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Mock - Deve retornar clientes com income igual ao informado")
    public void findByIncome_DeveRetornarClientesComIncomeMockado() throws Exception {
        double income = 1500.0;

        ClientDTO dto1 = new ClientDTO(1L, "Cliente A", "12345678900", income, Instant.parse("1990-01-01T00:00:00Z"), 1);
        ClientDTO dto2 = new ClientDTO(2L, "Cliente B", "98765432100", income, Instant.parse("1985-01-01T00:00:00Z"), 2);

        Mockito.when(service.findByIncome(income)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/clients/income/")
                .param("income", String.valueOf(income))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].income").value(income))
            .andExpect(jsonPath("$[1].income").value(income));
    }

    @Test
    @DisplayName("Mock - Deve retornar clientes com CPF parecido ao informado")
    public void findByCPFLike_DeveRetornarClientesMockados() throws Exception {
        String cpfLike = "123";

        ClientDTO dto = new ClientDTO(1L, "Cliente CPF", "12399988877", 3500.0, Instant.now(), 0);

        Mockito.when(service.findByCpfLike(cpfLike)).thenReturn(List.of(dto));

        mockMvc.perform(get("/clients/cpf-like/")
                .param("cpf", cpfLike)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].cpf").value("12399988877"));
    }

    @Test
    @DisplayName("Mock - Deve retornar 404 ao buscar cliente com ID inexistente")
    public void findById_DeveRetornarNotFound_QuandoIdNaoExiste() throws Exception {
        Long idInexistente = 999L;

        Mockito.when(service.findById(idInexistente))
               .thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/clients/{id}", idInexistente)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Mock - Deve retornar 404 ao tentar deletar cliente inexistente")
    public void delete_DeveRetornarNotFound_QuandoIdNaoExiste() throws Exception {
        Long idInexistente = 1000L;

        Mockito.doThrow(new ResourceNotFoundException("Cliente não encontrado"))
               .when(service).delete(idInexistente);

        mockMvc.perform(delete("/clients/{id}", idInexistente))
            .andExpect(status().isNotFound());
    }
}
