package com.orders.repositories;

import com.orders.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;

/**
 * Order Repository
 *
 * All the methods defined here are automatically auto-generated by Spring data.
 * If you need custom implementations, the process is:
 *  - Define another custom interface with those operations
 *  - Implement the previous interface in a new Class
 *  - Modify the Mongo Repository interface (this one, in this file), to extend the previous interface.
 *  This way, when you inject this interface, Spring will inject an implementation which contains your own
 *  implementation (for the custom interface), plus the auto-generated methods provided on-thefly by
 *  spring data.
 */

public interface OrderRepository extends MongoRepository<Order, String>, OrderRepositoryCustom {

    // An example of an Auto-generated method
    List<Order> findByCustomer(String customer);

    // An example of an auto-generated method wiht a custom query searching in a nested collection
    @Query("{ 'items.product' : ?0 }")
    List<Order> findOrdersByProduct(String product);

    // An example of a Projection, where one field is ruled out...
    @Query(value = "{}", fields="{items: 0}")
    List<Order> findOrdersWithoutItems();
}