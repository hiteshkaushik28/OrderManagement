package com.ordermgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @SequenceGenerator(name = "PROD_SEQUENCE", sequenceName = "PROD_SEQUENCE_ID", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PROD_SEQUENCE")
    private final long id;

    @Column
    private final String name;

    @Column
    private final double price;

    @SuppressWarnings("unused")
    public Product() {
        this("");
    }

    public Product(String name) {
        this(name, 0);
    }

    public Product(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, double price) {
        this.id = 0L;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return (id == 0L && name.equals(product.name) || id == product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name='" + name + ", price='" + price + '}';
    }
}
