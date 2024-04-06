package com.demo1.demo1.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientCod;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "client_email")
    private String email;

    public Client() {
    }

    public Client(String clientName, String email) {
        this.clientName = clientName;
        this.email = email;

    }

    public String getClientName() {
        return clientName;
    }

    public String getEmail() {
        return email;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
