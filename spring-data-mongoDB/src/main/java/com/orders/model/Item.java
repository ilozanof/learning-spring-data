package com.orders.model;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Represents One Imte wihtin an Order.
 * Take Notice that we don't have to use the @Document annotation here, since this object is
 * contained within the Order object already. for the same reason, we don't need an ID (we have an "id"
 * field, but it's not a MongodDB ID, since it's not annotation).
 *
 * In some Tests we are comparing the content of two collections of Objects, that's why
 * we have overriden the "hashCode" and "equals" methods.
 */
public class Item {
    private Long id;

    @Indexed    // Ony for academic purposes, no real effect in an embedded (in-memory) DB
    private String product;

    private int price;

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        boolean result = false;
        Item other = (Item) obj;
        result = id.equals(other.id) &&  product.equals(other.product) && price == other.price;
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
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
