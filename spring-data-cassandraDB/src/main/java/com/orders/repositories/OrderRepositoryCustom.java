package com.orders.repositories;

import com.orders.model.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findAllOrders();
}
