package com.buy_products.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.buy_products.demo.models.Product;
import com.buy_products.demo.services.ProductsService;
import com.buy_products.demo.services.StockService;
import com.buy_products.demo.services.TransactionExchangeService;

/**
 * Clase encargada de la compra de productos
 * Maneja las peticiones HTTP para compra de productos
 */
@RestController
public class BuyProducts {

    private final ProductsService productsService;

    private static class BuyProductsRequestBody {
        public String productName;
        public int quantity;
        public int clientCod;

    }

    @Autowired
    public BuyProducts(ProductsService productsService, TransactionExchangeService transactionExchangeService,
            StockService stockService) {
        this.productsService = productsService;
    }

    /**
     * Metodo utilizado para crear productos
     * y agregarlos al deposito de productos de manera `sincrona`
     * API REST encargada de crear los productos de manera sincrona
     * 
     * @param product El producto a crear, respeta la estructura de la clase
     *                Products
     * @return ResponseEntity<Map<String, Object>> Respuesta de la creacion del
     *         producto
     */

    @PostMapping("/create_product")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product) {
        try {
            if (product.getProductName() == null || product.getProductName().isEmpty()
                    || product.getDescription() == null || product.getDescription().isEmpty() || product.getPrice() <= 0
                    || product.getQuantity() <= 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Invalid input parameters");
                return ResponseEntity.badRequest().body(response);
            }
            Map<String, Object> response = productsService.insertProduct(product.getProductName(),
                    product.getDescription(), product.getPrice(), product.getQuantity());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Metodo utilizado para comprar productos.
     * Seria la API REST encargada de comprar los productos de manera asincrona
     * usando RabbitMQ para la gestion de mensajes.
     *
     * @param buyProductsRequestBody El producto a comprar y la cantidad. Debe ser
     *                               un objeto de tipo BuyProductsRequestBody que
     *                               contiene los siguientes campos:
     *                               - productName (String): El nombre del producto
     *                               a comprar.
     *                               - quantity (int): La cantidad del producto a
     *                               comprar.
     *                               - clientCod (int): El código del cliente que
     *                               realiza la compra.
     * @return ResponseEntity<Map<String, Object>> Respuesta de la compra del
     *         producto. Contiene un mapa con los siguientes campos:
     *         - status (String): El estado de la operación. Puede ser "success" o
     *         "error".
     *         - message (String): Un mensaje que describe el resultado de la
     *         operación.
     * @throws Exception Si ocurre un error durante la compra del producto.
     */
    @PostMapping("/buy_product")
    public ResponseEntity<Map<String, Object>> buyProduct(@RequestBody BuyProductsRequestBody buyProductsRequestBody) {
        /**
         * Verificamos si los parametros de entrada son validos
         */
        try {
            if (buyProductsRequestBody.productName == null || buyProductsRequestBody.productName.isEmpty()
                    || buyProductsRequestBody.quantity <= 0 || buyProductsRequestBody.clientCod <= 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Invalid input parameters");
                return ResponseEntity.badRequest().body(response);
            }

            /**
             * Verificamos que el producto exista, si no existe retornamos un error
             * porque no se puede comprar un producto que no existe
             */
            Product product = productsService.getProduct(buyProductsRequestBody.productName);
            if (product == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Product not found, create first");
                return ResponseEntity.ok(response);
            }
            /**
             * Enviamos el mensaje a la cola de mensajes para comprar el producto
             * utilizando RabbitMQ
             */
            productsService.sendBuyProductMessage(product.getProductName(), buyProductsRequestBody.quantity,
                    buyProductsRequestBody.clientCod, product.getProductCod());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Product stock increased successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

}
