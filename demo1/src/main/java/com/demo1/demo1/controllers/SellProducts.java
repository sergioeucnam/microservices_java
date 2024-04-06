package com.demo1.demo1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo1.demo1.models.Client;
import com.demo1.demo1.models.Product;
import com.demo1.demo1.services.ClientsService;
import com.demo1.demo1.services.ProductsService;
import com.demo1.demo1.services.TransactionExchangeService;

import java.util.Map;
import java.util.HashMap;

@RestController
public class SellProducts {

    private final ProductsService productsService;
    private final ClientsService clientsService;
    private final TransactionExchangeService transactionExchangeService;

    @Autowired
    public SellProducts(ProductsService productsService, ClientsService clientsService,
            TransactionExchangeService transactionExchangeService) {
        this.productsService = productsService;
        this.clientsService = clientsService;
        this.transactionExchangeService = transactionExchangeService;
    }

    private static class SellProductsRequest {
        public int clientCod;
        public int productCod;
        public int quantity;
    }

    @PostMapping("/create_client")
    public ResponseEntity<Map<String, Object>> createClient(@RequestBody Client client) {
        try {
            Boolean isValidEmail = client.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            if (client.getClientName() == null || client.getEmail() == null || client.getClientName().isEmpty()
                    || client.getEmail().isEmpty() || !isValidEmail) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Invalid input parameters");
                return ResponseEntity.ok(response);
            }
            Map<String, Object> response = clientsService.createClient(client.getClientName(), client.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error creating client: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/sell_products")
    public ResponseEntity<Map<String, Object>> sellProducts(@RequestBody SellProductsRequest request) {
        try {
            if (request.clientCod <= 0 || request.productCod <= 0 || request.quantity <= 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Invalid input parameters");
                return ResponseEntity.ok(response);
            }

            Product product = productsService.getProductById(request.productCod);
            if (product == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Product not found");
                return ResponseEntity.ok(response);
            }

            if (product.getQuantity() < request.quantity) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Not enough products in stock");
                return ResponseEntity.ok(response);
            }

            Client client = clientsService.getClientById(request.clientCod);
            if (client == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Client not found");
                return ResponseEntity.ok(response);
            }

            /**
             * Enviamos el mensaje a la cola de rabbitMQ para 
             * que el servicio de transacciones realice la transacción
             */
            productsService.sendSellProductMessage(request.quantity, request.clientCod, request.productCod,product.getPrice());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Products sold successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error selling products: " + e.getMessage());
            return ResponseEntity.ok(response);

        }
    }

}
