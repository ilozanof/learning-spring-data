package com.orders.repositories;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.orders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.List;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    @Autowired
    private CassandraOperations cassandraTemplate;

    public List<Order> findAllOrders() {
        String[] columns = new String[] {"id", "customer", "type"};
        Select select = QueryBuilder.select(columns).from("orderskeyspace.Order");
        List<Order> orders = cassandraTemplate.select(select, Order.class);
        return orders;
    }
}
