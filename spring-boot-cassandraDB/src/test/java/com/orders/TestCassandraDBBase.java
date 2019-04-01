package com.orders;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;


import java.io.IOException;
import java.util.Properties;

/**
 * Base class for the Cassandra Tests.
 *
 * It includes functionality to start/stop an embedded Cassandra DB, so the tests can run. Unlike with other No-SQL
 * DB, like Mongo, in this case the Embedded DB is not defined in the Configuration level, so it's NOT
 * automatically started/stopped when running Spring Boot. So we need to take care of starting and stopping it
 * every time we run the tests. Aslo we need to take care of crating/destroying the TABLES, User Defined Types, etc
 * that our App needs.
 *
 * This class implements methods to start and stop the server. Other Text Classes will take care of creating the Tables
 * needed for their use cases.
 */

public class TestCassandraDBBase {

    // Utility method to retrieve a property from "application.resources"...
    // Since we need the values in static methods, it's not possible to inject them via Spring, so
    // we load them manually...
    private static String getProperty(String name) throws IOException {
        Properties props = new Properties();
        props.load(TestCassandraDBBase.class.getClassLoader().getResourceAsStream("application.properties"));
        return props.getProperty(name);
    }

    @BeforeClass
    public static void startCassandraDB() throws Exception {

        String keystore         = getProperty("spring.data.cassandra.keyspace-name");
        String contact_pints    = getProperty("spring.data.cassandra.contact-points");
        String port             = getProperty("spring.data.cassandra.port");

        String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS " + keystore
                                            +  " WITH replication = { 'class': 'SimpleStrategy', "
                                            + "'replication_factor': '3' };";
        String KEYSPACE_ACTIVATE_QUERY = "USE " + keystore + ";";

        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        Cluster cluster = Cluster.builder()
                .addContactPoints(contact_pints)
                .withPort(Integer.parseInt(port))
                .build();
        Session session = cluster.connect();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute(KEYSPACE_ACTIVATE_QUERY);
    }

    @AfterClass
    public static void stopCassandraDB() {
        EmbeddedCassandraServerHelper.getSession();
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
