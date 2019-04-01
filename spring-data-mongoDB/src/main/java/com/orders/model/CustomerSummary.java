package com.orders.model;

/**
 * A class to store a Summary, which will be populated with the result of an Aggretation, after
 * running the aggregation in the MongoDB.
 *
 * Take notie that no anotations are needed here, sine this is only the container of some
 * information that is obtained from the Db, but it's not being stored in it.
 */

public class CustomerSummary {
    private String customer;
    private int numOrders;
    private double total;

    public String getCustomer() {
        return customer;
    }

    public int getNumOrders() {
        return numOrders;
    }

    public double getTotal() {
        return total;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setNumOrders(int numOrders) {
        this.numOrders = numOrders;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
