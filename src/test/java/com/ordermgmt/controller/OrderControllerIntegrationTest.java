package com.ordermgmt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ordermgmt.dto.OrderDto;
import com.ordermgmt.entity.OrderItem;
import com.ordermgmt.entity.Product;
import com.ordermgmt.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MockMvc mockMvc;

    private Product savedProduct1;
    private Product savedProduct2;
    private double price1;
    private double price2;
    private ObjectMapper mapper;

    @Before
    public void init() {
        price1 = 20.50;
        price2 = 40.50;
        Product product1 = new Product(11, "Product 1", price1);
        savedProduct1 = productRepository.saveAndFlush(product1);
        Product product2 = new Product(21, "Product 2", price2);
        savedProduct2 = productRepository.saveAndFlush(product2);
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Test
    public void testCreateOrder() throws Exception {
        int quantityOfItem1 = 2;
        int quantityOfItem2 = 1;
        OrderItem orderItem = new OrderItem(0L, savedProduct1, quantityOfItem1, null);
        OrderItem orderItem2 = new OrderItem(0L, savedProduct2, quantityOfItem2, null);

        Set<OrderItem> orderItems1 = new HashSet<>();
        orderItems1.add(orderItem);
        orderItems1.add(orderItem2);
        String buyersEmail = "buyer@gmail.com";

        OrderDto orderDto = new OrderDto(buyersEmail, LocalDateTime.now(), orderItems1);

        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String orderJson = ow.writeValueAsString(orderDto);

        double orderItem1Amount = price1 * quantityOfItem1;
        double orderItem2Amount = price2 * quantityOfItem2;

        double expectedTotalAmount = orderItem1Amount + orderItem2Amount;

        this.mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content(orderJson)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.buyersEmail").value(buyersEmail)).andExpect(jsonPath("$.orderItems").isArray()).andExpect(jsonPath("$.orderItems.length()").value(2)).andExpect(jsonPath("$.totalAmount").value(expectedTotalAmount));
    }
}
