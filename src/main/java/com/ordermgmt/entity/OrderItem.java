package com.ordermgmt.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import java.util.Objects;

@Entity
public class OrderItem {
    @Id
    @SequenceGenerator(name = "ORDER_ITEM_SEQUENCE", sequenceName = "ORDER_ITEM_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORDER_ITEM_SEQUENCE")
    private final long id;

    @OneToOne
    private final Product product;

    @Column
    private final int quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_parent")
    @JsonBackReference
    private Order parentOrder;

    public OrderItem(long id, Product product, int quantity, Order parentOrder) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.parentOrder = parentOrder;
    }

    @SuppressWarnings("unused")
    public OrderItem() {
        this(0L, new Product(), -1, new Order());
    }

    public OrderItem(Product product, int quantity) {
        this(0L, product, quantity, null);
    }

    public Order getParentOrder() {
        return parentOrder;
    }

    public void setParentOrder(Order parentOrder) {
        this.parentOrder = parentOrder;
    }

    double getAmount() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id &&
                quantity == orderItem.quantity &&
                product.equals(orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
