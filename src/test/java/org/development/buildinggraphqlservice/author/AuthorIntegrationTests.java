package org.development.buildinggraphqlservice.mongo;

import org.development.buildinggraphqlservice.check.Author;
import org.development.buildinggraphqlservice.check.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorIntegrationTests extends AbstractBaseIntegrationTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ExecutionGraphQlService executionGraphQlService;

    private ExecutionGraphQlServiceTester tester;

    @BeforeEach
    public void setUp() {
        authorRepository.save(new Author("author-1", "John", "Doe"));
        authorRepository.save(new Author("author-2", "Jane", "Doe"));
        authorRepository.save(new Author("author-3", "Jack", "Doe"));
        tester = ExecutionGraphQlServiceTester.create(executionGraphQlService);
    }

    @Test
    public void shouldVerifyExistingAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotEmpty();
        assertThat(authors).size().isEqualTo(3);
    }

    @Test
    public void shouldVerifyExistingAuthorsAgain() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).isNotEmpty();
        assertThat(authors).size().isEqualTo(3);
    }

    @Test
    public void shouldGetFirstAuthor() {
        tester
                .documentName("authorDetails")
                .variable("id", "author-1")
                .execute()
                .path("authorById")
                .matchesJson("""
                            {
                                "lastName": "Doe"
                             }
                          }
                        """);
    }

    @Test
    public void shouldReturnNullWhenAuthorNotFound() {
        tester
                .documentName("authorDetails")
                .variable("id", "non-existent-author-id")
                .execute()
                .path("authorById")
                .valueIsNull();
    }

    @Test
    public void shouldReturnNullWhenCaseDoesNotMatch() {
        tester
                .documentName("authorDetails")
                .variable("id", "Author-1")
                .execute()
                .path("authorById")
                .valueIsNull();
    }

    @Test
    public void shouldReturnNullWhenPathDoesNotExist() {
        tester
                .documentName("authorDetails")
                .variable("id", "author-1")
                .execute()
                .path("randomPath")
                .pathDoesNotExist();
    }
}
