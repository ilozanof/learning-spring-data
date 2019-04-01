package com.orders.model;



import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table(value = "Order")
public class Order {

    @PrimaryKey
    private UUID id;

    @Column
    private String customer;

    @Column
    private String type;


    public UUID getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public String getType() {
        return type;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setType(String type) {
        this.type = type;
    }
}
