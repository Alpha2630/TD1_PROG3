package com.alpha.product_management.entity;

import java.time.Instant;

public class Product {
    private Integer id;
    private String name;
    private Double price;
    private Instant creationDateTime;


    public Product() {}

    public Product(Integer id, String name, Double price, Instant creationDateTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDateTime = creationDateTime;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", creationDateTime=" + creationDateTime +
                '}';
    }
}