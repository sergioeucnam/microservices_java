package com.demo1.demo1.repositories;

import org.springframework.data.repository.CrudRepository;

import com.demo1.demo1.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findByProductName(String productName);

}
