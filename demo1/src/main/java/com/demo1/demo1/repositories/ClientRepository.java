package com.demo1.demo1.repositories;

import org.springframework.data.repository.CrudRepository;

import com.demo1.demo1.models.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

    Client findByClientName(String clientName);

    Boolean existsByEmail(String email);

}
