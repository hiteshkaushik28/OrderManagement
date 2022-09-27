package com.ordermgmt.service;

import com.ordermgmt.entity.Product;
import com.ordermgmt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No product has Id " + id));
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(new Product(product.getName(), product.getPrice()));
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        Product matchedProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No product has Id " + id));
        return productRepository.save(new Product(id, product.getName(), product.getPrice()));
    }
}
