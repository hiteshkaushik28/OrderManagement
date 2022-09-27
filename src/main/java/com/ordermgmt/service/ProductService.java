package com.ordermgmt.service;

import com.ordermgmt.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();

    Optional<Product> getProductById(Long id);

    Product addProduct(Product product);

    Optional<Product> updateProduct(Product product, Long id);
}
