package com.demo1.demo1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exchange_transactions")
public class TransactionExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tarnsactionExchangeCod;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "client_cod")
    private int clientCod;
    @Column(name = "product_cod")
    private int productCod;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "value")
    private double value;

    public TransactionExchange() {
    }

    public TransactionExchange(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getClientCod() {
        return clientCod;
    }

    public void setClientCod(int clientCod) {
        this.clientCod = clientCod;
    }

    public int getProductCod() {
        return productCod;
    }

    public void setProductCod(int productCod) {
        this.productCod = productCod;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getValues() {
        return value;
    }

    public void setValues(double value) {
        this.value = value;
    }

}
