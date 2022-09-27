package com.ordermgmt.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ordermgmt.entity.Product;
import com.ordermgmt.repository.OrderItemRepository;
import com.ordermgmt.repository.OrderRepository;
import com.ordermgmt.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    private static final String PRODUCT_NAME = "FirstProduct";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MockMvc mockMvc;

    private Product firstProduct;

    @Before
    public void init() {
        Product initialProduct = productRepository.saveAndFlush(new Product(PRODUCT_NAME));
        firstProduct = productRepository.findById(initialProduct.getId()).orElseThrow(() -> new AssertionError("Initial Product ought to be persisted"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> all = productRepository.findAll();
        int expected = all.size();

        ResultActions resultActions = this.mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(expected));

        String jsonString = resultActions.andReturn().getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        List<Product> newProduct = mapper.readValue(jsonString, new TypeReference<>() {
        });

        Optional<Product> optInitialProduct = newProduct.stream()
                .filter(product -> product.getId() == this.firstProduct.getId())
                .findFirst();

        if (optInitialProduct.isEmpty()) {
            throw new AssertionError("First product should be present");
        } else {
            Product firstProduct = optInitialProduct.get();
            Assert.assertEquals(firstProduct, this.firstProduct);
        }
    }

    @Test
    public void testCreateNewProduct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String newProdName = "New Product";
        String prodJson = ow.writeValueAsString(new Product(newProdName));

        ResultActions resultActions = this.mockMvc.perform(post("/products")
                        .contentType(APPLICATION_JSON)
                        .content(prodJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newProdName));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Product newProduct = mapper.readValue(contentAsString, Product.class);

        Assert.assertTrue("New products Id cannot be zero", newProduct.getId() != 0L);

        try {
            productRepository.getOne(newProduct.getId());
        } catch (EntityNotFoundException nfe) {
            throw new AssertionError(newProduct + " has not been persisted");
        }
    }

    @Test
    public void shouldUpdateAnExistingProduct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String newProdName = "New Product";
        String prodJson = ow.writeValueAsString(new Product(newProdName));

        this.mockMvc.perform(put("/product/" + firstProduct.getId())
                        .contentType(APPLICATION_JSON)
                        .content(prodJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newProdName))
                .andExpect(jsonPath("$.id").value(firstProduct.getId()));
    }

    @Test
    public void shouldUpdateAnExistingProductInvalidId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String newProdName = "New Product";
        String prodJson = ow.writeValueAsString(new Product(newProdName));

        this.mockMvc.perform(put("/product/" + 100)
                        .contentType(APPLICATION_JSON)
                        .content(prodJson))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductByIdInvalidId() throws Exception {
        this.mockMvc.perform(get("/product/" + 100))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
