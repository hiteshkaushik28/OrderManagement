package com.ordermgmt.service;

import com.ordermgmt.dto.OrderDto;
import com.ordermgmt.entity.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(OrderDto orderDto);

    List<Order> getOrders();
}
