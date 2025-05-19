package com.iftm.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Buscar cliente por nome exato (ignorando case)
    @Query("SELECT obj FROM Client obj WHERE LOWER(obj.name) = LOWER(:name)")
    Client findByNameIgnoreCase(@Param("name") String name);

    // Buscar clientes por parte do nome (LIKE, ignorando case)
    @Query("SELECT obj FROM Client obj WHERE LOWER(obj.name) LIKE LOWER(CONCAT('%', :namePart, '%'))")
    List<Client> findByNameContainingIgnoreCase(@Param("namePart") String namePart);

    // Buscar clientes com salário superior a um valor
    List<Client> findByIncomeGreaterThan(Double income);

    // Buscar clientes com salário inferior a um valor
    List<Client> findByIncomeLessThan(Double income);

    // Buscar clientes com salário entre dois valores
    List<Client> findByIncomeBetween(Double minIncome, Double maxIncome);

    // Buscar clientes por data de nascimento entre duas datas
    List<Client> findClientByBirthDateBetween(Instant start, Instant end);
}
