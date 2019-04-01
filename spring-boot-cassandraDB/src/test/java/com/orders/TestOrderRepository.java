package com.orders;


import com.datastax.driver.core.utils.UUIDs;
import com.orders.model.Item;
import com.orders.model.Order;
import com.orders.repositories.OrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.cassandra.core.CassandraAdminOperations;


import org.springframework.data.cassandra.core.cql.generator.CreateUserTypeCqlGenerator;
import org.springframework.data.cassandra.core.cql.keyspace.CreateUserTypeSpecification;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Test class for the Order Repository.
 * Before and after each Test case, it initilzes the DataModel, creating the Tables and User defined types needed.
 *
 * NOTA: Unlike wiht other embedded DB like H2, this time we NEED to create the Model before we run the tests (in
 * our tests with the embedded H2 DB, the DB Model seems to be crated automatically as the POJOs are mapped into
 * Persistent entities).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraDBConfig.class)
public class TestOrderRepository extends TestCassandraDBBase {


    @Autowired
    private CassandraAdminOperations adminTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CassandraMappingContext mappingContext;


    @Before
    public void initDBModel() {

        CreateUserTypeSpecification itemUserType = mappingContext
                .getCreateUserTypeSpecificationFor(mappingContext.getPersistentEntity(Item.class));

        adminTemplate.getCqlOperations().execute(CreateUserTypeCqlGenerator.toCql(itemUserType));

        adminTemplate.createTable(true,
                mappingContext.getPersistentEntity(Order.class).getTableName(),
                Order.class,
                new HashMap<String, Object>());
    }

    @After
    public void clearDBModel() {
        adminTemplate.dropTable(mappingContext.getPersistentEntity(Order.class).getTableName());
        CreateUserTypeSpecification itemUserType = mappingContext
                .getCreateUserTypeSpecificationFor(mappingContext.getPersistentEntity(Item.class));
        adminTemplate.dropUserType(itemUserType.getName());
    }

    private Order createOrder() {
        Order result = new Order();
        result.setId(UUIDs.timeBased());
        String type = new Random().nextInt(2) == 0 ? "WEB" : "SHOP";
        String customer = new Random().nextInt(2) == 0 ? "Dummy Customer" : "Real Customer";
        result.setType(type);
        result.setCustomer(customer);
        return result;
    }

    public void addOrders(int numOrders) {
        for (int i = 0; i < numOrders; i++) {
            orderRepository.save(createOrder());
        }
    }

    @Test
    public void testAddOrders() {
        addOrders(2000);
    }

    @Test
    public void testReadAllOrders() {
        final int NUM_ORDERS = 2000;
        addOrders(NUM_ORDERS);

        List<Order> orders = orderRepository.findAll();
        assertNotNull(orders);
        assertEquals(orders.size(), NUM_ORDERS);
    }

}
