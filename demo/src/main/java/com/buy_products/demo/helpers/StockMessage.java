package com.buy_products.demo.helpers;

import java.io.Serializable;

public class StockMessage implements Serializable {
    private int quantity;
    private int clientCod;
    private String productName;
    private double price;
    private int productCod;
    private String transactionType;

    public StockMessage() {

    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getClientCod() {
        return clientCod;
    }

    public void setClientCod(int clientCod) {
        this.clientCod = clientCod;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductCod() {
        return productCod;
    }

    public void setProductCod(int productCod) {
        this.productCod = productCod;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }
}