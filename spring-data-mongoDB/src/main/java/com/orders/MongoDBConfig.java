package com.orders;

import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

/**
 * Java-configuration class for MongoDB.
 *
 * We are using an mebedded MongoDB instance, look at the "mongoClient()" method.
 */

@Configuration
@EnableMongoRepositories(basePackages = "com.orders.repositories")
public class MongoDBConfig {

    @Bean
    public MongoClient mongoClient() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp("localhost");
        MongoClient mongoClient = mongo.getObject();
        return mongoClient;
    }

    public MongoDbFactory mongoDbFactory() throws IOException {
        SimpleMongoDbFactory result = new SimpleMongoDbFactory(mongoClient(), "OrdersDB");
        return result;
    }

    @Bean
    public MongoTemplate mongoTemplate() throws IOException  {
       return new MongoTemplate(mongoClient(), "OrdersDB");
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws IOException {
        return new MongoTransactionManager(mongoDbFactory());
    }
}
