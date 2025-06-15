package com.iftm.client.repositories;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepository repository;

    private Long idExistente;
    private Long idNaoExistente;
    private PageRequest pageRequest;

    private Client client;
    private Client clientWithTargetIncome;
    private Double targetIncome;

    private ClientDTO clientDTOtoUpdate;

    @BeforeEach
    void setUp() throws Exception {
        repository.deleteAll(); // limpa o banco para garantir isolamento dos testes

        // Cliente padrão para vários testes
        client = new Client(null, "Cliente Test", "12345678901", 2000.0, Instant.now(), 0);
        clientWithTargetIncome = new Client(null, "Cliente Alvo", "09876543210", 2000.0, Instant.now(), 1);

        client = repository.save(client);
        clientWithTargetIncome = repository.save(clientWithTargetIncome);

        idExistente = client.getId();
        idNaoExistente = 1000L;

        pageRequest = PageRequest.of(0, 10);
        targetIncome = 2000.0;

        clientDTOtoUpdate = new ClientDTO(null, "Nome Atualizado", "99988877700", 5000.0, Instant.parse("2001-10-20T00:00:00Z"), 2);
    }

    /* delete deveria
    ◦ retornar vazio quando o id existir
    ◦ lançar uma EmptyResultDataAccessException quando o id não existir */
    @Test
    public void apagarNaoDeveFazerNadaQuandoIdExistente() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(idExistente);
        });

        Optional<Client> result = repository.findById(idExistente);
        Assertions.assertFalse(result.isPresent(), "O cliente deveria ter sido deletado");
    }

    @Test
    void apagarDeveLancarUmaExcecaoQuandoIdNaoExistente() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(idNaoExistente);
        });
    }

    // findAllPaged deveria retornar uma página com todos os clientes (e chamar o método findAll do repository)
    @Test
    void findAllPageDeveRetornarPaginaComTodosOsClientes() {
        Page<ClientDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(2, result.getTotalElements(), "Deveria haver 2 clientes na página");
        Assertions.assertFalse(result.getContent().isEmpty(), "A lista de clientes não deve estar vazia");

        boolean hasClientName = result.getContent().stream().anyMatch(dto -> dto.getName().equals(client.getName()));
        Assertions.assertTrue(hasClientName, "Deveria conter o cliente inserido");
    }

    // Deve retornar página vazia se não houver clientes com o income informado
    @Test
    public void findByIncomeDeveRetornarPaginaVaziaQuandoNaoHouverClientesComIncome() {
        Double incomeInexistente = 99999.0;
        Page<ClientDTO> result = service.findByIncome(incomeInexistente, pageRequest);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertTrue(result.isEmpty(), "A página deve estar vazia para income inexistente");
        Assertions.assertEquals(0, result.getTotalElements(), "Não deve haver clientes com esse income");
    }

    // Deve retornar página vazia quando não houver clientes cadastrados
    @Test
    public void findAllPagedDeveRetornarPaginaVaziaQuandoNaoHouverClientes() {
        repository.deleteAll();
        Page<ClientDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertTrue(result.isEmpty(), "A página deve estar vazia quando não há clientes");
    }

    // Deve garantir que update não altera outros clientes
    @Test
    public void updateNaoDeveAlterarOutrosClientes() {
        Long outroId = clientWithTargetIncome.getId();
        String nomeOriginal = clientWithTargetIncome.getName();
        service.update(idExistente, clientDTOtoUpdate);

        Client outroCliente = repository.findById(outroId).orElse(null);
        Assertions.assertNotNull(outroCliente, "Outro cliente deve existir");
        Assertions.assertEquals(nomeOriginal, outroCliente.getName(), "Outro cliente não deve ser alterado");
    }

    // Deve garantir que insert persiste todos os campos corretamente
    @Test
    public void insertDevePersistirTodosOsCamposCorretamente() {
        Instant birthDate = Instant.parse("1990-01-01T00:00:00Z");
        ClientDTO dto = new ClientDTO(null, "Teste Campos", "11122233344", 1234.56, birthDate, 3);
        ClientDTO result = service.insert(dto);

        Assertions.assertNotNull(result.getId(), "O ID gerado não deve ser nulo");
        Assertions.assertEquals(dto.getName(), result.getName());
        Assertions.assertEquals(dto.getCpf(), result.getCpf());
        Assertions.assertEquals(dto.getIncome(), result.getIncome());
        Assertions.assertEquals(dto.getBirthDate(), result.getBirthDate());
        Assertions.assertEquals(dto.getChildren(), result.getChildren());
    }

    // Deve garantir que findById retorna todos os campos corretamente
    @Test
    public void findByIdDeveRetornarTodosOsCamposCorretamente() {
        ClientDTO result = service.findById(idExistente);

        Assertions.assertEquals(client.getName(), result.getName(), "O nome deve ser igual");
        Assertions.assertEquals(client.getCpf(), result.getCpf(), "O CPF deve ser igual");
        Assertions.assertEquals(client.getIncome(), result.getIncome(), "A renda deve ser igual");
        Assertions.assertEquals(client.getBirthDate(), result.getBirthDate(), "A data de nascimento deve ser igual");
        Assertions.assertEquals(client.getChildren(), result.getChildren(), "O número de filhos deve ser igual");
    }

    // Deve garantir que delete remove o cliente do banco
    @Test
    public void apagarDeveRemoverClienteDoBanco() {
        service.delete(idExistente);
        Optional<Client> result = repository.findById(idExistente);
        Assertions.assertFalse(result.isPresent(), "O cliente deveria ter sido removido do banco");
    }
    @Test
    public void updateDeveRetornarClientDTOQuandoIdExistir() {
        ClientDTO result = service.update(idExistente, clientDTOtoUpdate);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(clientDTOtoUpdate.getName(), result.getName(), "O nome do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getCpf(), result.getCpf(), "O CPF do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getIncome(), result.getIncome(), "A renda do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getChildren(), result.getChildren(), "O número de filhos do cliente atualizado deve ser igual");
    }

    @Test
    public void updateDeveLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(idNaoExistente, clientDTOtoUpdate);
        });
    }

    // insert deveria retornar um ClientDTO ao inserir um novo cliente
    @Test
    public void insertDeveRetornarClientDTOAoInserirNovoCliente() {
        ClientDTO newClientInputDTO = new ClientDTO(
            null,
            "Novo Cliente Inserido",
            "12345678900",
            3500.0,
            Instant.now(),
            1
        );

        ClientDTO resultDTO = service.insert(newClientInputDTO);

        Assertions.assertNotNull(resultDTO, "O resultado não deve ser nulo");
        Assertions.assertNotNull(resultDTO.getId(), "O ID gerado não deve ser nulo");

        Assertions.assertEquals(newClientInputDTO.getName(), resultDTO.getName(), "O nome do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getCpf(), resultDTO.getCpf(), "O CPF do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getIncome(), resultDTO.getIncome(), "A renda do cliente inserida deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getChildren(), resultDTO.getChildren(), "O número de filhos do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getBirthDate(), resultDTO.getBirthDate(), "A data de nascimento do cliente inserido deve ser igual");

        Client entidade = repository.findById(resultDTO.getId()).orElse(null);
        Assertions.assertNotNull(entidade, "O cliente deve existir no banco de dados");
        Assertions.assertEquals(newClientInputDTO.getName(), entidade.getName(), "O nome persistido deve ser igual");
    }
}
