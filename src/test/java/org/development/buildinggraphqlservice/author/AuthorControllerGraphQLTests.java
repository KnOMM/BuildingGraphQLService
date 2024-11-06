package org.development.buildinggraphqlservice.author;

import org.development.buildinggraphqlservice.AuthorController;
import org.development.buildinggraphqlservice.check.Author;
import org.development.buildinggraphqlservice.check.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorRepository authorRepository;

    private List<Author> authors;

    @BeforeEach
    public void setUp() {
        authors = new ArrayList<>(List.of(
                new Author("author-1", "John", "Doe"),
                new Author("author-2", "Jane", "Doe"),
                new Author("author-3", "Jack", "Doe")
        ));
    }

    @Test
    public void testGraphQLQueryForAuthor() throws Exception {
        // Prepare your GraphQL query as a JSON body
        String query = """
                {
                    "query": "query GetAuthor($id: ID!) { authorById(id: $id) { firstName lastName }}",
                    "variables": { "id": "author-1" }
                }
                """;

        when(authorRepository.findById(anyString())).thenReturn(Optional.ofNullable(authors.get(1)));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query))

                .andExpect(status().isOk()) // Expect a successful HTTP status (200 OK)
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.authorById.firstName").value("Jane")) // Check the first name
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.authorById.lastName").value("Doe")); // Check the last name
        verify(authorRepository, times(1)).findById(anyString());
    }

    @Test
    public void testGraphQLQueryForAuthorNotFound() throws Exception {
        // Prepare your GraphQL query as a JSON body
        String query = """
            {
                "query": "query GetAuthor($id: ID!) { authorById(id: $id) { firstName lastName }}",
                "variables": { "id": "author-not-found" }
            }
            """;

        when(authorRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(query))
                .andExpect(status().isOk())  // Expect a 200 status
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.authorById").value(nullValue()));

        verify(authorRepository, times(1)).findById(anyString());
    }
}
