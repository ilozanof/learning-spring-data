package com.orders.repositories;

import com.orders.model.CustomerSummary;
import com.orders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Implementation of the Custom interface.
 *
 * If you follow the guidelines in the file "OrderRepository.java", this implementation will be added by Spring to
 * the Auto-generated implementation of the REpository, when you inject the "OrderRepository" interface in your
 * classes.
 */
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Order> findOrdersByType(String type) {
        List<Order> result = null;
        String typeValue = type.equals("NET") ? "WEB" : type;
        Criteria where = Criteria.where("type").is(typeValue);
        Query query = Query.query(where);

        result = mongoTemplate.find(query, Order.class);
        return result;
    }


    @Override
    public List<CustomerSummary> getCustomerSummaries() {
        List<CustomerSummary> result = null;

        // Explanation on how to group the results:
        /*
        First, we ut the items one level up, using the "unwind" operation:

        Aggregation.unwind("items")

        . That way, if we start with these structure:
        [
            {
                id: 1,
                customer: "John",
                items: [
                    { product: "Table", price: 20 }, { product: "chair", price: 5 }
                ]
            },
            ... more orders...
        ]

        .. it's transformed into this:

        [
            {
                id: 1,
                customer: "John",
                items: { product: "Table", price: 20 }
            },
            {
                id: 1,
                customer: "John",
                items: { product: "chair", price: 5 }
            },
            ... more Orders...
        ]

        .. this way, the "items" go up one level, so we are replicating now the Orders, but each one of them has now
        a single ITEM.

        Over the previous structure, we can calculate some aggregations ans return some info:

         Aggregation.group("id")
                    .last("id").as("orderId")
                    .last("customer").as("customer")
                    .sum("items.price").as("orderTotalPrice"),

        , this way, we are getting this result:

        [
            {
                orderId: 1,
                customer: "John",
                orderTotalPrice: 25
            }
        ]

        , so we have now a list of ORDER SUmmaries. Now, we operate over the previous structure, but this time we
        group the results by CUSTOMER...

        Aggregation.group("customer")
                        .last("customer").as("customer")
                        .sum("orderTotalPrice").as("total")
                        .count().as("numOrders")

         , and finally that will get as the following result, which can be mapped into the CustomerSummary class:

         [
            {
                customer: "John",
                total: 25,
                numOrders: 1
            }
         ]
         */

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.unwind("items"),
                Aggregation.group("id")
                    .last("id").as("orderId")
                    .last("customer").as("customer")
                    .sum("items.price").as("orderTotalPrice"),
                Aggregation.group("customer")
                        .last("customer").as("customer")
                        .sum("orderTotalPrice").as("total")
                        .count().as("numOrders")
        );

        result = mongoTemplate.aggregate(agg, Order.class, CustomerSummary.class).getMappedResults();

        return result;
    }

}
