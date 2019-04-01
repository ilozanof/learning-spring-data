package com.orders.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents an Order in an normal Order Management System
 * Look at how we use the Sprng and Mongo annotations here.
 *
 * In some Tests we are comparing the content of two collections of Objects, that's why
 * we have overriden the "hashCode" and "equals" methods.
 */
@Document
public class Order {

    @Id
    private String id;

    @Field("client") // Override the default field name. Ony for academic purposes, no real effect
    @Indexed         // Ony for academic purposes, no real effect in an embedded (in-memory) DB
    private String customer;

    private String type;

    // A collection of items within the ORder. In the Tests we'll show how to perform queries using the
    // fields in the collection, or how to make use of Aggregations with them.
    private List<Item> items = new ArrayList<>();

    @Override
    public int hashCode() {
        return Integer.parseInt(id);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        boolean result = false;
        Order other = (Order) obj;
        result = this.id.equals(other.id)
                && this.customer.equals(other.customer)
                && this.type.equals(other.type);
        if (result) {
            result = (items != null && other.items != null) || (items == null && other.items == null);
            if (result && items != null && (items.size() == other.items.size()))
                for (int i = 0; i < items.size(); i++) result = result && items.get(i).equals(other.items.get(i));
        }
        return result;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public String getType() {
        return type;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
