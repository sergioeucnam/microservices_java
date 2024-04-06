package com.buy_products.demo.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Esta clase es un helper que verifica la conexión a la base de datos.
 * Se ejecuta al inicio de la aplicación y realiza una consulta simple ("SELECT
 * USER();") para comprobar la conexión.
 * Si la conexión es exitosa, imprime la versión de MySQL en la consola.
 * Si la conexión falla, imprime el error en la consola.
 */
@Component
public class TestDBConection implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUser;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Override
    public void run(String... args) throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con = java.sql.DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
            String query = "SELECT USER();";
            java.sql.Statement stmt = con.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println("Connection successful MySQL USER: " + rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Connection failed with error: " + e.getMessage());
        }
    }
}