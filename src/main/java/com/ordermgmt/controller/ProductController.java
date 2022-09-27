package com.ordermgmt.controller;

import com.ordermgmt.entity.Product;
import com.ordermgmt.service.ProductService;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SwaggerDefinition(
        info = @Info(
                description = "Used to manage Available Products",
                version = "1.0.0-SNAPSHOT",
                title = "The Product Manager"
        ),
        consumes = {"application/json"},
        produces = {"application/json"},
        schemes = {SwaggerDefinition.Scheme.HTTP}
)
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<Object> listProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getProductById(@ApiParam(value = "The ID of the product", required = true, example = "1") @PathVariable long id) {
        var res = productService.getProductById(id);
        if (res.isEmpty())
            return new ResponseEntity<>("No product exists with id " + id, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(res.get(), HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<Object> addProduct(@RequestBody Product newProduct) {
        return new ResponseEntity<>(productService.addProduct(newProduct), HttpStatus.OK);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Object> updateProduct(@RequestBody Product updatedProduct, @ApiParam(value = "The ID of the product to be updated", required = true, example = "1") @PathVariable long id) {
        var res = productService.updateProduct(updatedProduct, id);
        if (res.isEmpty())
            return new ResponseEntity<>("No product exists with id " + id, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(res.get(), HttpStatus.OK);
    }

}
