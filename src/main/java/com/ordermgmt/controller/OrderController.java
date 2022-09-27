package com.ordermgmt.controller;

import com.ordermgmt.dto.OrderDto;
import com.ordermgmt.entity.OrderItem;
import com.ordermgmt.service.OrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SwaggerDefinition(
        info = @Info(
                description = "Manage products and orders",
                version = "1.0.0-SNAPSHOT",
                title = "Order Management API"
        ),
        consumes = {"application/json"},
        produces = {"application/json"},
        schemes = {SwaggerDefinition.Scheme.HTTP}
)
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<Object> viewOrders() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "orderData",
                    dataType = "OrderDtoBodyType"
            )
    )
    @PostMapping("/orders")
    public ResponseEntity<Object> addOrder(@RequestBody OrderDto orderData) {
        return new ResponseEntity<>(orderService.saveOrder(orderData), HttpStatus.OK);
    }

    public static class OrderDtoBodyType {
        @ApiModelProperty(example = "buyer@gmail.com")
        private String email;

        @ApiModelProperty(dataType = "OrderItemType", name = "orderItems", example = "[{\"product\":{\"id\":1},\"quantity\":1}]")
        private List<OrderItem> orderItems;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }
    }
}
