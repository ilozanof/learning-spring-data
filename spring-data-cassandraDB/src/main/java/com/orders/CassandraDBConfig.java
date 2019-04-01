package com.orders;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories
public class CassandraDBConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "orderskeyspace";
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {"com.orders.model"};
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints("127.0.0.1");
        cluster.setPort(9142);
        return cluster;
    }

    @Bean
    @Override
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        BasicCassandraMappingContext mappingContext = new BasicCassandraMappingContext();
        mappingContext.setInitialEntitySet(getInitialEntitySet());
        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), getKeyspaceName()));
        return mappingContext;
    }

}
