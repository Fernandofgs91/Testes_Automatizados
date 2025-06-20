package com.iftm.client.resources;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.iftm.client.services.ClientService;

//necessário para utilizar o MockMVC
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientResourceIntegrationTest {
    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private ClientService service;

    /**
     * Caso de testes : Verificar se o endpoint get/clients/ retorna todos os clientes existentes
     * Arrange:
     * - base de dado : 12 clientes
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Conceição Evaristo', '10619244881', 1500.0, TIMESTAMP WITH TIME ZONE '2020-07-13T20:50:00Z', 2);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Lázaro Ramos', '10619244881', 2500.0, TIMESTAMP WITH TIME ZONE '1996-12-23T07:00:00Z', 2);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Clarice Lispector', '10919444522', 3800.0, TIMESTAMP WITH TIME ZONE '1960-04-13T07:50:00Z', 2);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Carolina Maria de Jesus', '10419244771', 7500.0, TIMESTAMP WITH TIME ZONE '1996-12-23T07:00:00Z', 0);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Gilberto Gil', '10419344882', 2500.0, TIMESTAMP WITH TIME ZONE '1949-05-05T07:00:00Z', 4);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Djamila Ribeiro', '10619244884', 4500.0, TIMESTAMP WITH TIME ZONE '1975-11-10T07:00:00Z', 1);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Jose Saramago', '10239254871', 5000.0, TIMESTAMP WITH TIME ZONE '1996-12-23T07:00:00Z', 0);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Toni Morrison', '10219344681', 10000.0, TIMESTAMP WITH TIME ZONE '1940-02-23T07:00:00Z', 0);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Yuval Noah Harari', '10619244881', 1500.0, TIMESTAMP WITH TIME ZONE '1956-09-23T07:00:00Z', 0);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Chimamanda Adichie', '10114274861', 1500.0, TIMESTAMP WITH TIME ZONE '1956-09-23T07:00:00Z', 0);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Silvio Almeida', '10164334861', 4500.0, TIMESTAMP WITH TIME ZONE '1970-09-23T07:00:00Z', 2);
INSERT INTO tb_client (name, cpf, income, birth_date, children) VALUES('Jorge Amado', '10204374161', 2500.0, TIMESTAMP WITH TIME ZONE '1918-09-23T07:00:00Z', 0);     * - Uma PageRequest default
     * @throws Exception 
     */
    @Test
    @DisplayName("Verificar se o endpoint get/clients/ retorna todos os clientes existentes")
    public void testarEndPointListarTodosClientesRetornaCorreto() throws Exception{
        //arrange
        int quantidadeClientes = 12;
        int quantidadeLinhasPagina = 12;

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
            .andExpect(jsonPath("$.totalElements").value(quantidadeClientes))
            .andExpect(jsonPath("$.numberOfElements").exists())
            .andExpect(jsonPath("$.numberOfElements").value(quantidadeLinhasPagina))
            .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(4,10,3,1,6,5,12,7,2,11,8,9)));
    }

    @Test
    @DisplayName("Deve retornar um cliente pelo ID existente")
    public void findById_DeveRetornarCliente_QuandoIdExistente() throws Exception {
        Long existingId = 3L;
        mockMVC.perform(get("/clients/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(existingId))
            .andExpect(jsonPath("$.name").value("Clarice Lispector"))
            .andExpect(jsonPath("$.cpf").value("10919444522"))
            .andExpect(jsonPath("$.income").value(3800.0))
            .andExpect(jsonPath("$.birthDate").value("1960-04-13T07:50:00Z"))
            .andExpect(jsonPath("$.children").value(2));
    }

    @Test
    @DisplayName("Deve retornar erro 404 ao buscar por ID inexistente")
    public void findById_DeveRetornarNotFound_QuandoIdInexistente() throws Exception {
        Long nonExistingId = 33L;
        mockMVC.perform(get("/clients/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Resource not found"))
            .andExpect(jsonPath("$.message").value("Entity not found"))
            .andExpect(jsonPath("$.path").value("/clients/id/33"));
    }

    @Test
    @DisplayName("Deve retornar clientes com income igual ao informado")
    public void findByIncome_DeveRetornarClientesComIncomeInformado() throws Exception {
        double income = 1500.0;
        mockMVC.perform(get("/clients/income/")
                .param("income", String.valueOf(income))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[*].income", everyItem(is(income))));
    }

    @Test
    @DisplayName("Deve retornar clientes com income maior que o informado")
    public void findByIncomeGreaterThan_DeveRetornarClientesComIncomeMaiorQueInformado() throws Exception {
        double income = 5000.0;
        mockMVC.perform(get("/clients/income-greater/")
                .param("income", String.valueOf(income))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[*].income", everyItem(greaterThan(income))));
    }

    @Test
    @DisplayName("Deve retornar clientes com CPF parecido com o informado")
    public void findByCPFLike_DeveRetornarClientesComCPFParcial() throws Exception {
        String cpfLike = "109";
        mockMVC.perform(get("/clients/cpf-like/")
                .param("cpf", cpfLike)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[*].cpf", everyItem(containsString(cpfLike))));
    }

    @Test
    @DisplayName("Deve inserir um novo cliente e retornar o objeto criado")
    public void insert_DeveRetornarClienteCriado() throws Exception {
        var clientDTO = new com.iftm.client.dto.ClientDTO(null, "Novo Cliente", "12345678901", 2000.0, java.time.Instant.parse("2000-01-01T00:00:00Z"), 1);
        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(clientDTO);
        mockMVC.perform(post("/clients/")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Novo Cliente"))
            .andExpect(jsonPath("$.cpf").value("12345678901"));
    }

    @Test
    @DisplayName("Deve deletar cliente existente e retornar 204")
    public void delete_DeveRetornarNoContent_QuandoIdExistente() throws Exception {
        Long existingId = 3L;
        mockMVC.perform(delete("/clients/{id}", existingId))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar deletar cliente inexistente")
    public void delete_DeveRetornarNotFound_QuandoIdInexistente() throws Exception {
        Long nonExistingId = 33L;
        mockMVC.perform(delete("/clients/{id}", nonExistingId))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar cliente existente e retornar objeto atualizado")
    public void update_DeveRetornarClienteAtualizado_QuandoIdExistente() throws Exception {
        Long existingId = 3L;
        var clientDTO = new com.iftm.client.dto.ClientDTO(null, "Cliente Atualizado", "10919444522", 4000.0, java.time.Instant.parse("1960-04-13T07:50:00Z"), 3);
        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(clientDTO);
        mockMVC.perform(put("/clients/{id}", existingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Cliente Atualizado"))
            .andExpect(jsonPath("$.children").value(3));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar cliente inexistente")
    public void update_DeveRetornarNotFound_QuandoIdInexistente() throws Exception {
        Long nonExistingId = 33L;
        var clientDTO = new com.iftm.client.dto.ClientDTO(null, "Cliente Atualizado", "00000000000", 4000.0, java.time.Instant.parse("2000-01-01T00:00:00Z"), 1);
        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(clientDTO);
        mockMVC.perform(put("/clients/{id}", nonExistingId)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Resource not found"));
    }
}
