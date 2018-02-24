package org.staydigital.services.banking.api;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.mongodb.MongoClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan({"org.staydigital.services.banking.aggregates","org.staydigital.services.banking.api"})
public class BankingService {

	private MongoClient mongoClient;
	
	@Autowired
	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BankingService.class, args);
	}
	
	@Bean
	public EventStorageEngine eventStore(MongoClient client) {
		return new MongoEventStorageEngine(new DefaultMongoTemplate(client));
	}

}
