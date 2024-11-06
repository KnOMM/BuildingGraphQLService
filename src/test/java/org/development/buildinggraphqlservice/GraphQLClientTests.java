package org.development.buildinggraphqlservice;

import org.development.buildinggraphqlservice.author.AbstractBaseIntegrationTest;
import org.development.buildinggraphqlservice.check.Author;
import org.development.buildinggraphqlservice.check.AuthorRepository;
import org.development.buildinggraphqlservice.check.BookStatic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;


import org.springframework.web.context.WebApplicationContext;

public class GraphQLClientTests extends AbstractBaseIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private HttpGraphQlTester tester;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setup() {
        // Set up MockMvcWebTestClient to simulate GraphQL requests without a live server
        WebTestClient client = MockMvcWebTestClient.bindToApplicationContext(context)
                .configureClient()
                .baseUrl("/graphql") // Set the base URL to your GraphQL endpoint
                .build();

        authorRepository.save(new Author("author-1", "John", "Doe"));
        authorRepository.save(new Author("author-2", "Jane", "Doe"));
        authorRepository.save(new Author("author-3", "Jack", "Doe"));

        tester = HttpGraphQlTester.create(client); // Create the GraphQL tester
    }

    @Test
    public void GivenValidDocument_WhenRequestingForBook_ThenThreeBooksReturned() {
        // Define the GraphQL query as a string
        String document = """
                    query bookDetails {
                      books {
                        name
                        pageCount
                        author {
                          id
                        }
                      }
                    }
                """;

        // Execute the query and assert that the number of books is exactly 3
        tester.document(document)
                .execute()
                .path("books")
                .entityList(BookStatic.class)
                .hasSize(3); // Assert exactly 3 books are returned
    }

    @Test
    public void GivenValidDocument_WhenRequestingForAuthor_ThenThreeAuthorsReturned() {
        tester
                .documentName("authorDetails")
                .variable("id", "author-1")
                .execute()
                .path("authorById.lastName")
                .entity(String.class)
                .isEqualTo("Doe");

//                .matchesJson("""
//                            {
//                                "lastName": "Doe"
//                             }
//                          }
//                        """)
    }
}
