package com.ordermgmt.service;

import com.ordermgmt.dto.OrderDto;
import com.ordermgmt.entity.Order;
import com.ordermgmt.entity.OrderItem;
import com.ordermgmt.entity.Product;
import com.ordermgmt.repository.OrderRepository;
import com.ordermgmt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order saveOrder(OrderDto orderDto) {
        Map<Long, Product> retrievedProducts = productRepository.findAllById(
                orderDto.getOrderItems().stream()
                        .map(item -> item.getProduct().getId())
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Product::getId, product -> product));

        if (retrievedProducts.size() != orderDto.getOrderItems().size()) return null;

        Set<OrderItem> newItems = orderDto.getOrderItems()
                .stream()
                .map(item -> new OrderItem(retrievedProducts.get(item.getProduct().getId()), item.getQuantity()))
                .collect(Collectors.toSet());

        Order orderToSave = new Order(0L, LocalDateTime.now(), orderDto.getEmail());
        orderToSave.addOrderItems(newItems);
        return orderRepository.save(orderToSave);
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
