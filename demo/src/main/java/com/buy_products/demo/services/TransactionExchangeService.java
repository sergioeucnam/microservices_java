package com.buy_products.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buy_products.demo.models.TransactionExchange;
import com.buy_products.demo.repositories.TransactionExchangeRepository;

/**
 * Esta clase se encarga de insertar los registros
 * de transacciones en la base de datos, para compra
 * y venta de productos
 */
@Service
public class TransactionExchangeService {

    private final TransactionExchangeRepository transactionExchangeRepository;

    @Autowired
    public TransactionExchangeService(TransactionExchangeRepository transactionExchangeRepository) {
        this.transactionExchangeRepository = transactionExchangeRepository;
    }

    public void insertTransaction(int clientCod, int productCod, int quantity, String transactionType, double price) {
        try {
            TransactionExchange transaction = new TransactionExchange();
            transaction.setClientCod(clientCod);
            transaction.setProductCod(productCod);
            transaction.setQuantity(quantity);
            transaction.setTransactionType(transactionType);
            transaction.setValues(price * quantity);
            transactionExchangeRepository.save(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
