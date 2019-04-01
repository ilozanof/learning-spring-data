package com.orders.repositories;

import com.orders.MongoDBConfig;
import com.orders.model.CustomerSummary;
import com.orders.model.Item;
import com.orders.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Testing Class for the OrderRepository, wich also includes the custom interface "OrderRepositoryCustom".
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MongoDBConfig.class)
// This fails! Is it because we are using a embedded MongoDB server or what?
//@Transactional
public class TestOrderRepository {

    @Autowired
    OrderRepository orderRepo;

    // Convenience method to add some test data into the DB...
    private void addOrders(int numOrders) {
        for (int i = 0; i < numOrders; i++) {
            Order order = new Order();
            order.setId(String.valueOf(i));
            int randomNum = new Random().nextInt(2);
            String customer = randomNum == 1 ? "Dummy Customer" : "Real Customer";
            order.setCustomer(customer);
            String type = randomNum == 0 ? "WEB" : "SHOP";
            order.setType(type);
            // Some Dummy Items...
            List<Item> items = new ArrayList<>();
            int numItems = new Random().nextInt(10) + 1;
            for (int b = 0;b < numItems; b++) {
                Item item = new Item();
                item.setId((long)b);
                //item.setOrder(order); // Check what happens if we leave this empty
                item.setPrice(new Random().nextInt(10) + 1);
                item.setProduct("Product " + b);
                items.add(item);
            } // for...
            order.setItems(items);
            orderRepo.save(order);
        } // for...
    }

    // Before each Test, we clean the DB so we start fresh
    @Before
    public void cleanDB() {
        orderRepo.deleteAll();
    }

    @Test
    public void testCreatingOrders() {
        addOrders(300);
    }

    @Test
    public void readOrders() {
        final int NUM_ORDERS = 30;
        addOrders(NUM_ORDERS);
        List<Order> orders = orderRepo.findAll();

        assertNotNull(orders);
        assertEquals(orders.size(), NUM_ORDERS);
    }

    @Test
    public void testFindByCustomer() {
        final int NUM_ORDERS = 2;
        final String customer = "Dummy Customer";
        addOrders(NUM_ORDERS);
        List<Order> orders = orderRepo.findByCustomer(customer);

        assertNotNull(orders);
        assertEquals(orders.size(), 1);
        assertEquals(orders.get(0).getCustomer(), customer);
    }

    @Test
    public void testFindByType() {
        final int NUM_ORDERS = 200;
        final String type1 = "NET";
        final String type2 = "WEB";
        addOrders(NUM_ORDERS);

        List<Order> orders1 = orderRepo.findOrdersByType(type1);
        List<Order> orders2 = orderRepo.findOrdersByType(type2);

        assertTrue((orders1 == null && orders2  == null ||(orders1 != null && orders2 != null)));
        assertEquals(orders1.size(), orders2.size());
        assertTrue(orders1.containsAll(orders2));
    }

    // NOTE: There does not seem to be an improvement here, but maybe its because we are using an embedded MongoDB,
    // so it all runs in memory.
    @Test
    public void testIndexesPerformance() {
        final int NUM_ORDERS = 10000;
        final String type1 = "NET";
        final String type2 = "WEB";
        addOrders(NUM_ORDERS);

        // We perform the same query several times, and we calclate the average time...
        final int NUM_SEARCHES = 5;
        long addedTime = 0;
        for (int i = 0; i < NUM_SEARCHES; i++) {
            long milisecsStart = System.currentTimeMillis();
            List<Order> orders = orderRepo.findOrdersByProduct("Product 2");
            long milisecsEnd = System.currentTimeMillis();
            addedTime += (milisecsEnd - milisecsStart);
            System.out.println(" Search " + i + " processed.");
        }
        long average = addedTime / NUM_SEARCHES;
        System.out.println("Time taken: " + average + " milisecs in average");
    }

    @Test
    public void testProjection() {
        final int NUM_ORDERS = 100;
        addOrders(NUM_ORDERS);

        List<Order> orders = orderRepo.findOrdersWithoutItems();

        assertNotNull(orders);
        assertEquals(orders.size(), NUM_ORDERS);
        orders.stream().forEach(o -> assertTrue(o.getItems() == null || o.getItems().size() == 0));
    }

    @Test
    public void testCustomerAggregation() {
        final int NUM_ORDERS = 4;
        addOrders(NUM_ORDERS);
        List<Order> ordersAll = orderRepo.findAll();
        List<CustomerSummary> customerSummaries = orderRepo.getCustomerSummaries();

        assertNotNull(customerSummaries);

    }
}
