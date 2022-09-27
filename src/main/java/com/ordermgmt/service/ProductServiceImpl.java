package com.ordermgmt.service;

import com.ordermgmt.entity.Product;
import com.ordermgmt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(new Product(product.getName(), product.getPrice()));
    }

    @Override
    public Optional<Product> updateProduct(Product product, Long id) {
        Optional<Product> res = productRepository.findById(id);
        if (res.isEmpty()) return Optional.empty();
        return Optional.of(productRepository.save(new Product(id, product.getName(), product.getPrice())));
    }
}
