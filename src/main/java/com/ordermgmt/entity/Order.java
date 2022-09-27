package com.ordermgmt.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Order_")
public class Order {
    @Id
    @SequenceGenerator(name = "ORDER_SEQUENCE", sequenceName = "ORDER_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORDER_SEQUENCE")
    private final long id;

    @Column(name = "CREATED_AT")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime createdAt;

    @Column
    private final String buyersEmail;

    @Column
    private double totalAmount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_parent")
    private final Set<OrderItem> orderItems = new HashSet<>();

    public Order(long id, LocalDateTime createdAt, String buyersEmail) {
        this.id = id;
        this.createdAt = createdAt;
        this.buyersEmail = buyersEmail;
    }

    Order() {
        this(0L, LocalDateTime.now(), "");
    }

    private double updateTotalAmount(@NotNull Set<OrderItem> orderItems) {
        return orderItems.stream().mapToDouble(OrderItem::getAmount).sum();
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBuyersEmail() {
        return buyersEmail;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addOrderItems(Set<OrderItem> items) {
        items.forEach(item -> item.setParentOrder(this));
        this.orderItems.addAll(items);
        this.totalAmount = updateTotalAmount(this.orderItems);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                buyersEmail.equals(order.buyersEmail) &&
                orderItems.equals(order.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
