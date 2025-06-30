package com.iftm.client.resources;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.services.ClientService;

//necessário para utilizar o MockMVC
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientResourceTest {
    @Autowired
    private MockMvc mockMVC;

    @MockBean
    private ClientService service;

    /**
     * Caso de testes : Verificar se o endpoint get/clients/ retorna todos os clientes existentes
     * Arrange:
     * - camada service simulada com mockito
     * - base de dado : 3 clientes
     * new Client(7l, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
     * new Client(4l, "Carolina Maria de Jesus", "10419244771", 7500.0, Instant.parse("1996-12-23T07:00:00Z"), 0);
     * new Client(8l, "Toni Morrison", "10219344681", 10000.0, Instant.parse("1940-02-23T07:00:00Z"), 0);
     * - Uma PageRequest default
     * @throws Exception 
     */
    @Test
    @DisplayName("Verificar se o endpoint get/clients/ retorna todos os clientes existentes")
    public void testarEndPointListarTodosClientesRetornaCorreto() throws Exception{
        //arrange
        int quantidadeClientes = 3;
        //configurando o Mock ClientService
        List<ClientDTO> listaClientes;
        listaClientes = new ArrayList<ClientDTO>();
        listaClientes.add(new ClientDTO(new Client(7L, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0)));
        listaClientes.add(new ClientDTO(new Client(4L, "Carolina Maria de Jesus", "10419244771", 7500.0, Instant.parse("1996-12-23T07:00:00Z"), 0)));
        listaClientes.add(new ClientDTO(new Client(8L, "Toni Morrison", "10219344681", 10000.0,Instant.parse("1940-02-23T07:00:00Z"), 0)));

        Page<ClientDTO> page = new PageImpl<>(listaClientes);

        Mockito.when(service.findAllPaged(Mockito.any())).thenReturn(page);        
        //fim configuração mockito

        //act

        ResultActions resultados = mockMVC.perform(get("/clients/").accept(MediaType.APPLICATION_JSON));

        //assign
        resultados
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[?(@.id == '%s')]",7L).exists())
            .andExpect(jsonPath("$.content[?(@.id == '%s')]",4L).exists())
            .andExpect(jsonPath("$.content[?(@.id == '%s')]",8L).exists())
            .andExpect(jsonPath("$.content[?(@.name == '%s')]","Toni Morrison").exists())
            .andExpect(jsonPath("$.totalElements").exists())
            .andExpect(jsonPath("$.totalElements").value(quantidadeClientes));


    }

    @Test
    @DisplayName("Deve retornar um cliente pelo ID existente (mock)")
    public void findById_DeveRetornarCliente_QuandoIdExistente() throws Exception {
        Long existingId = 7L;
        ClientDTO clientDTO = new ClientDTO(new Client(existingId, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0));
        Mockito.when(service.findById(existingId)).thenReturn(clientDTO);
        mockMVC.perform(get("/clients/{id}", existingId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(existingId))
            .andExpect(jsonPath("$.name").value("Jose Saramago"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar por ID inexistente (mock)")
    public void findById_DeveRetornarNotFound_QuandoIdInexistente() throws Exception {
        Long nonExistingId = 99L;
        Mockito.when(service.findById(nonExistingId)).thenThrow(new com.iftm.client.services.exceptions.ResourceNotFoundException("Entity not found"));
        mockMVC.perform(get("/clients/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve inserir um novo cliente e retornar o objeto criado (mock)")
    public void insert_DeveRetornarClienteCriado() throws Exception {
        ClientDTO clientDTO = new ClientDTO(null, "Novo Cliente", "12345678901", 2000.0, Instant.parse("2000-01-01T00:00:00Z"), 1);
        ClientDTO clientSalvo = new ClientDTO(20L, "Novo Cliente", "12345678901", 2000.0, Instant.parse("2000-01-01T00:00:00Z"), 1);
        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(clientDTO);
        Mockito.when(service.insert(Mockito.any())).thenReturn(clientSalvo);
        mockMVC.perform(post("/clients/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(20L))
            .andExpect(jsonPath("$.name").value("Novo Cliente"));
    }

    @Test
    @DisplayName("Deve deletar cliente existente e retornar 204 (mock)")
    public void delete_DeveRetornarNoContent_QuandoIdExistente() throws Exception {
        Long existingId = 7L;
        Mockito.doNothing().when(service).delete(existingId);
        mockMVC.perform(delete("/clients/{id}", existingId))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar deletar cliente inexistente (mock)")
    public void delete_DeveRetornarNotFound_QuandoIdInexistente() throws Exception {
        Long nonExistingId = 99L;
        Mockito.doThrow(new com.iftm.client.services.exceptions.ResourceNotFoundException("Entity not found")).when(service).delete(nonExistingId);
        mockMVC.perform(delete("/clients/{id}", nonExistingId))
            .andExpect(status().isNotFound());
    }
}
