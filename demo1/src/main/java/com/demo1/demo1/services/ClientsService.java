package com.demo1.demo1.services;

import org.springframework.stereotype.Service;

import com.demo1.demo1.models.Client;
import com.demo1.demo1.repositories.ClientRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientsService {

    private final ClientRepository clientRepository;

    public ClientsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Map<String, Object> createClient(String clientName, String email) {
        try {
            Map<String, Object> response = new HashMap<>();
            if (clientRepository.existsByEmail(email)) {
                response.put("status", "error");
                response.put("message", "Client with email already exists");
                return response;
            }
            Client createdClient = new Client(clientName, email);
            clientRepository.save(createdClient);
            response.put("status", "success");
            response.put("message", "Client created successfully");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Error creating client");
        return response;
    }

    public Client getClientById(int clientCod) {
        try {
            Long id = Long.valueOf(clientCod);
            return clientRepository.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
