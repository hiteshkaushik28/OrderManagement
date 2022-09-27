package com.ordermgmt.entity;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class OrderTest {

    @Test
    public void getTotalAmount() {
        Product product1 = new Product(1L, "Samsung galaxy", 20.50);
        Product product2 = new Product(2L, "nike shoes", 30.50);
        OrderItem orderItem = new OrderItem(1L, product1, 2, null);
        OrderItem orderItem2 = new OrderItem(2L, product2, 1, null);
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem);
        orderItems.add(orderItem2);
        Order order = new Order(1L, LocalDateTime.now(), "buyer@gmail.com");
        order.addOrderItems(orderItems);
        Assert.assertEquals(order.getTotalAmount(), 71.50, 0);
    }
}
