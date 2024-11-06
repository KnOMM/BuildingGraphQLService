package org.development.buildinggraphqlservice.author;

import org.development.buildinggraphqlservice.BuildingGraphQlServiceApplication;
import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = BuildingGraphQlServiceApplication.class)
public class AbstractBaseIntegrationTest {
    @Container
    static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:7.0.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        mongoContainer.start();
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "testdb");
    }

    @AfterAll
    static void tearDown() {
        mongoContainer.stop();
    }
}
