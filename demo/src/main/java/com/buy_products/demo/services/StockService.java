package com.buy_products.demo.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buy_products.demo.helpers.StockMessage;
import java.util.Map;

@Service
public class StockService {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private TransactionExchangeService transactionExchangeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void increaseStock(String productId, int quantity) {
    }

    public void sendMessage(String queue, StockMessage message) {
        rabbitTemplate.convertAndSend(queue, message);
    }

    /**
     * Metodo encargado de recibir los mensajes de la cola stockQueue, para realizar
     * la compra de productos, e insertar un registro de la transaccion
     * 
     * @param message Mensaje recibido de la cola stockQueue
     * @return void No retorna nada
     */
    @RabbitListener(queues = "stockQueue")
    public void receiveMessage(StockMessage message) {
        try {
            if (!"buy".equals(message.getTransactionType())) {
                System.out.println("Transaction type: " + message.getTransactionType());
                return;
            }

            /*
             * Realiza la compra de productos, se encarga de aumentar el stock de los
             * productos
             */
            productsService.buyProducts(message.getProductName(), message.getQuantity());
            /*
             * Inserta un registro representando la transaccion en la tabla
             * exchange_transactions
             */
            transactionExchangeService.insertTransaction(message.getClientCod(), message.getProductCod(),
                    message.getQuantity(),
                    "buy", message.getPrice());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}