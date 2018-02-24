package org.staydigital.services.banking.config;


import com.mongodb.MongoClient;
import org.axonframework.eventsourcing.eventstore.EventStore;

import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoTemplate;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AnnotationDriven;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


@AnnotationDriven
public class AxonConfiguration {

    @Autowired
    public MongoClient mongo;

    @Value("${spring.application.databaseName}")
    private String databaseName;

    @Value("${spring.application.eventsCollectionName}")
    private String eventsCollectionName;

    @Value("${spring.application.snapshotCollectionName}")
    private String snapshotCollectionName;

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate() {
        MongoTemplate template = new DefaultMongoTemplate(mongo,
                databaseName, eventsCollectionName, snapshotCollectionName);
        return template;
    }

    @Bean
    JacksonSerializer axonJsonSerializer() {
        return new JacksonSerializer();
    }
}
