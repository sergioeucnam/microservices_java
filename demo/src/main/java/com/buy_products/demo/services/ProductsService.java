package com.buy_products.demo.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buy_products.demo.helpers.StockMessage;
import com.buy_products.demo.models.Product;
import com.buy_products.demo.repositories.ProductRepository;

@Service
public class ProductsService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductsService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String getProducts() {
        return "These are the products";
    }

    /**
     * Inserta el producto dentro de la tabla products
     * representa la operación de insertar un producto en la BBDD
     * Se pueden crear productos con el mismo nombre, pero en ese caso
     * solo se aumentara el stock, para mas practicidad
     * 
     * @param productName
     * @param description
     * @param price
     * @param quantity
     * @return
     */
    public Map<String, Object> insertProduct(String productName, String description, double price, int quantity) {
        try {
            Map<String, Object> response = new HashMap<>();
            Product product = productRepository.findByProductName(productName);
            if (product != null) {
                product.setQuantity(product.getQuantity() + quantity);
                productRepository.save(product);
                response.put("status", "success");
                response.put("message", "Product already exists, stock updated");

                return response;
            } else {
                productRepository.save(new Product(productName, description, price, quantity));
                response.put("status", "success");
                response.put("message", "Product inserted successfully");
                return response;
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error inserting product: " + e.getMessage());
            return response;
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

    /**
     * Metodo utilizado para obtener un producto por su nombre
     * 
     * @param productName Nombre del producto
     * @return Producto encontrado
     */
    public Product getProduct(String productName) {
        try {
            return productRepository.findByProductName(productName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Metodo utilizado para enviar un mensaje a la cola de mensajes stockQueue
     * para comprar un producto de manera asincrona
     * 
     * @param productName Nombre del producto
     * @param quantity    Cantidad del producto a comprar
     * @param clientCod   Codigo del cliente que realiza la compra
     * @param productCod  Codigo del producto a comprar
     */
    public void sendBuyProductMessage(String productName, int quantity, int clientCod, int productCod) {
        try {
            StockMessage message = new StockMessage();
            message.setProductName(productName);
            message.setQuantity(quantity);
            message.setClientCod(clientCod);
            message.setProductCod(productCod);
            message.setTransactionType("buy");
            rabbitTemplate.convertAndSend("stockQueue", message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * ????????????????????
     */
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

}
