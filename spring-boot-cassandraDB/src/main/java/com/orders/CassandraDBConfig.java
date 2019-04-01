package com.orders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@PropertySource("classpath:application.properties")
@EnableCassandraRepositories
public class CassandraDBConfig extends AbstractCassandraConfiguration {


    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace_name;

    @Value("${spring.data.cassandra.contact-points}")
    private String contact_points;

    @Value("${spring.data.cassandra.port}")
    private String port;

    @Override
    protected String getKeyspaceName() {
        return keyspace_name;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {"com.orders.model"};
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(contact_points);
        cluster.setPort(Integer.parseInt(port));
        return cluster;
    }

    /*
    @Bean
    @Override
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        BasicCassandraMappingContext mappingContext = new BasicCassandraMappingContext();
        mappingContext.setInitialEntitySet(getInitialEntitySet());
        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), getKeyspaceName()));
        return mappingContext;
    }
    */
    /*
    @Override
    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }
    */
}
