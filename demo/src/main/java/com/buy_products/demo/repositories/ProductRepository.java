package com.buy_products.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.buy_products.demo.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    /**
     * Busca un producto por su nombre
     * 
     * @param productName Nombre del producto
     * @return Producto encontrado
     */
    Product findByProductName(String productName);

}
