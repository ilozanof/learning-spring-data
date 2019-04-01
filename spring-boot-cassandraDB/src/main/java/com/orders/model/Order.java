package com.orders.model;




import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table(value = "order")
public class Order {

    @PrimaryKey
    private UUID id;

    @Column
    private String customer;

    @Column
    private String type;

    //@CassandraType(type = DataType.Name.LIST, typeArguments = DataType.Name.UDT, userTypeName = "item")
    private List<Item> items;

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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
