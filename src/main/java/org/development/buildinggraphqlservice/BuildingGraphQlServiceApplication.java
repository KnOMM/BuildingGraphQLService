package org.development.buildinggraphqlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class BuildingGraphQlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuildingGraphQlServiceApplication.class, args);
    }

}
