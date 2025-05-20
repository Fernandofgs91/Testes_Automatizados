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
    /**
     * <p><b>Cenário:</b> Buscar cliente com nome exatamente igual, mas com letras em maiúsculas/minúsculas diferentes.</p>
     * <p><b>Entrada:</b> "maria silva"</p>
     * <p><b>Saída esperada:</b> Cliente encontrado com nome "Maria Silva"</p>
     */
    void testFindByNameIgnoreCase_Existente() {
        Client result = repository.findByNameIgnoreCase("maria silva");
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Maria Silva");
    }

    @Test
    @DisplayName("Deve retornar null para nome inexistente")
    /**
     * <p><b>Cenário:</b> Buscar cliente por nome inexistente no banco de dados.</p>
     * <p><b>Entrada:</b> "nome inexistente"</p>
     * <p><b>Saída esperada:</b> null</p>
     */
    void testFindByNameIgnoreCase_NaoExistente() {
        Client result = repository.findByNameIgnoreCase("nome inexistente");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Deve retornar clientes com parte do nome similar ao texto informado (existente)")
    /**
     * <p><b>Cenário:</b> Buscar clientes que contenham parte do nome informada, ignorando maiúsculas/minúsculas.</p>
     * <p><b>Entrada:</b> "maria"</p>
     * <p><b>Saída esperada:</b> Lista com os clientes "Maria Silva" e "Ana Maria"</p>
     */
    void testFindByNameContainingIgnoreCase_Existente() {
        List<Client> result = repository.findByNameContainingIgnoreCase("maria");
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia para parte do nome inexistente")
    /**
     * <p><b>Cenário:</b> Buscar clientes com parte do nome que não corresponde a nenhum nome existente.</p>
     * <p><b>Entrada:</b> "xyz"</p>
     * <p><b>Saída esperada:</b> Lista vazia</p>
     */
    void testFindByNameContainingIgnoreCase_NaoExistente() {
        List<Client> result = repository.findByNameContainingIgnoreCase("xyz");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar todos os clientes ao buscar por nome vazio")
    /**
     * <p><b>Cenário:</b> Buscar clientes com parte do nome sendo string vazia.</p>
     * <p><b>Entrada:</b> "" (vazio)</p>
     * <p><b>Saída esperada:</b> Lista com todos os 3 clientes cadastrados</p>
     */
    void testFindByNameContainingIgnoreCase_Vazio() {
        List<Client> result = repository.findByNameContainingIgnoreCase("");
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("Deve retornar clientes com salário superior a um valor")
    /**
     * <p><b>Cenário:</b> Buscar clientes com salário superior a 2500.</p>
     * <p><b>Entrada:</b> 2500.0</p>
     * <p><b>Saída esperada:</b> Lista com "Maria Silva" e "João Souza"</p>
     */
    void testFindByIncomeGreaterThan() {
        List<Client> result = repository.findByIncomeGreaterThan(2500.0);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar clientes com salário inferior a um valor")
    /**
     * <p><b>Cenário:</b> Buscar clientes com salário inferior a 3000.</p>
     * <p><b>Entrada:</b> 3000.0</p>
     * <p><b>Saída esperada:</b> Lista com apenas "Ana Maria"</p>
     */
    void testFindByIncomeLessThan() {
        List<Client> result = repository.findByIncomeLessThan(3000.0);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Ana Maria");
    }

    @Test
    @DisplayName("Deve retornar clientes com salário entre dois valores")
    /**
     * <p><b>Cenário:</b> Buscar clientes com salário entre 2000 e 4000.</p>
     * <p><b>Entrada:</b> 2000.0 e 4000.0</p>
     * <p><b>Saída esperada:</b> Lista com "Maria Silva" e "Ana Maria"</p>
     */
    void testFindByIncomeBetween() {
        List<Client> result = repository.findByIncomeBetween(2000.0, 4000.0);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve retornar clientes com data de nascimento entre duas datas")
    /**
     * <p><b>Cenário:</b> Buscar clientes com data de nascimento entre 2010 e a data atual.</p>
     * <p><b>Entrada:</b> "2010-01-01T00:00:00Z" até Instant.now()</p>
     * <p><b>Saída esperada:</b> Lista com "Maria Silva" e "Ana Maria"</p>
     */
    void testFindClientByBirthDateBetween() {
        Instant dataI = Instant.parse("2010-01-01T00:00:00Z");
        Instant dataT = Instant.now();
        List<Client> result = repository.findClientByBirthDateBetween(dataI, dataT);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Deve atualizar cliente e verificar alterações")
    /**
     * <p><b>Cenário:</b> Atualizar nome, salário e data de nascimento de um cliente existente.</p>
     * <p><b>Entrada:</b> Nome: "Maria Souza", Salário: 3500.0, Data: "2018-01-01"</p>
     * <p><b>Saída esperada:</b> Cliente com dados atualizados persistido no banco</p>
     */
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
