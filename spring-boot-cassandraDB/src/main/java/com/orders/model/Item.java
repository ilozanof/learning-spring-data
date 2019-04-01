package com.orders.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("item")
public class Item {
    private Long id;
    private String product;
    private int price;

    public Long getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public int getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
