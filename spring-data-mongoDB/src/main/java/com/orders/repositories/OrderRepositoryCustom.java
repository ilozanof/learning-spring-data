package com.orders.repositories;

import com.orders.model.CustomerSummary;
import com.orders.model.Order;

import java.util.List;

/**
 * A custom interface, with additional oerations that need to be implemented in a customized way.
 */
public interface OrderRepositoryCustom {

    List<Order> findOrdersByType(String type);

    // A method for showing how aggregations work
    List<CustomerSummary> getCustomerSummaries();
}
