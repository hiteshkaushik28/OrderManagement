package com.ordermgmt.service;

import com.ordermgmt.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);
    Product addProduct(Product product);

    Product updateProduct(Product product, Long id);
}
