package com.demo1.demo1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo1.demo1.helpers.StockMessage;
import com.demo1.demo1.models.Client;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.demo1.demo1.models.Product;
import com.demo1.demo1.repositories.ClientRepository;
import com.demo1.demo1.repositories.ProductRepository;

import java.util.Map;
import java.util.HashMap;

@Service
public class ProductsService {
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ProductsService(ProductRepository productRepository, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    public Map<String, Object> sellProducts(int clientCod, int productCod, int quantity) {
        /**
         * Esta operacion debe realizarse de manera asincrona usando
         * mensajes en una cola de mensajes de RabbitMQ :D
         * dentro del receptor de mensajes se debe realizar la operacion\
         * aca esta como ejemplo para ahcerlo de manera sincrona
         */
        try {
            Long id = Long.valueOf(productCod);
            Product product = productRepository.findById(id).get();
            System.out.println(product.getQuantity());
            if (product.getQuantity() < quantity) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Not enough products in stock");
                return response;
            } else {
                // Client client = clientRepository.findById(clientCod).get();
                // if (client == null) {
                // Map<String, Object> response = new HashMap<>();
                // response.put("status", "error");
                // response.put("message", "Client not found");
                // return response;
                // }
                product.setQuantity(product.getQuantity() - quantity);
                productRepository.save(product);
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Products sold successfully");
                return response;
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error selling products: " + e.getMessage());
            return response;
        }
    }

    public Product getProductById(int productCod) {
        try {
            Long id = Long.valueOf(productCod);
            return productRepository.findById(id).get();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Metodo utilizado para enviar un mensaje a la cola de mensajes stockQueue
     * para vender un producto de manera asincrona
     * 
     * @param productName Nombre del producto
     * @param quantity    Cantidad del producto a comprar
     * @param clientCod   Codigo del cliente que realiza la compra
     * @param productCod  Codigo del producto a comprar
     */
    public void sendSellProductMessage(int quantity, int clientCod, int productCod, double price) {
        try {
            StockMessage message = new StockMessage();
            message.setQuantity(quantity);
            message.setClientCod(clientCod);
            message.setProductCod(productCod);
            message.setPrice(price);
            message.setTransactionType("sell");
            rabbitTemplate.convertAndSend("stockQueue", message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metodo utilizado para comprar productos, los productos se compran de manera
     * sincrona
     * solo se podra comprar si el producto ya existe en la BBDD, en este caso por
     * practicidad
     * se utiliza la key Name como identificador del producto, podria ser un codigo
     * de barras.
     * 
     * @param productName
     * @param quantity
     * @return
     */

    public Map<String, Object> buyProducts(String productName, int quantity) {
        try {
            /**
             * Verifica si el producto existe, si no existe
             * retorna un mensaje de error
             */
            Map<String, Object> response = new HashMap<>();
            Product product = productRepository.findByProductName(productName);
            if (product == null) {
                response.put("status", "error");
                response.put("message", "Product not found, create first");
                return response;
            }
            /**
             * Aumenta el stock del producto en la BBDD
             * y retorna un mensaje de exito
             */
            product.setQuantity(product.getQuantity() + quantity);

            productRepository.save(product);
            response.put("status", "success");
            response.put("message", "Product stock increased successfully");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error increasing product stock: " + e.getMessage());
            return response;
        }
    }

}
