package com.iftm.client;

import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository repository;

    private Client client1;
    private Client client2;
    private Client client3;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        client1 = new Client(null, "Maria Silva", "12345678900", 3000.0, Instant.parse("2017-12-25T20:30:50Z"));
        client2 = new Client(null, "João Souza", "98765432100", 5000.0, Instant.parse("2010-05-10T10:00:00Z"), null);
        client3 = new Client(null, "Ana Maria", "11122233344", 2000.0, Instant.parse("2020-01-01T00:00:00Z"), null);
        repository.save(client1);
        repository.save(client2);
        repository.save(client3);
    }

    @Test
    @DisplayName("Deve retornar cliente pelo nome exato, ignorando case")
    void testFindByNameIgnoreCase_Existente() {
        Client result = repository.findByNameIgnoreCase("maria silva");
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Maria Silva");
    }

    @Test
    @DisplayName("Deve retornar null para nome inexistente")
    void testFindByNameIgnoreCase_NaoExistente() {
        Client result = repository.findByNameIgnoreCase("nome inexistente");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve retornar clientes com parte do nome similar ao texto informado (existente)")
    void testFindByNameContainingIgnoreCase_Existente() {
        List<Client> result = repository.findByNameContainingIgnoreCase("maria");
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia para parte do nome inexistente")
    void testFindByNameContainingIgnoreCase_NaoExistente() {
        List<Client> result = repository.findByNameContainingIgnoreCase("xyz");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar todos os clientes ao buscar por nome vazio")
    void testFindByNameContainingIgnoreCase_Vazio() {
        List<Client> result = repository.findByNameContainingIgnoreCase("");
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Deve retornar clientes com salário superior a um valor")
    void testFindByIncomeGreaterThan() {
        List<Client> result = repository.findByIncomeGreaterThan(2500.0);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar clientes com salário inferior a um valor")
    void testFindByIncomeLessThan() {
        List<Client> result = repository.findByIncomeLessThan(3000.0);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Ana Maria");
    }

    @Test
    @DisplayName("Deve retornar clientes com salário entre dois valores")
    void testFindByIncomeBetween() {
        List<Client> result = repository.findByIncomeBetween(2000.0, 4000.0);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar clientes com data de nascimento entre duas datas")
    void testFindClientByBirthDateBetween() {
        Instant dataI = Instant.parse("2010-01-01T00:00:00Z");
        Instant dataT = Instant.now();
        List<Client> result = repository.findClientByBirthDateBetween(dataI, dataT);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve atualizar cliente e verificar alterações")
    void testUpdateClient() {
        client1.setName("Maria Souza");
        client1.setIncome(3500.0);
        client1.setBirthDate(Instant.parse("2018-01-01T00:00:00Z"));
        repository.save(client1);
        Client updated = repository.findByNameIgnoreCase("maria souza");
        assertThat(updated).isNotNull();
        assertThat(updated.getIncome()).isEqualTo(3500.0);
        assertThat(updated.getBirthDate()).isEqualTo(Instant.parse("2018-01-01T00:00:00Z"));
    }
}
