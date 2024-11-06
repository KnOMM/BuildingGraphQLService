package org.development.buildinggraphqlservice.mongo;

import org.development.buildinggraphqlservice.AuthorController;
import org.development.buildinggraphqlservice.check.Author;
import org.development.buildinggraphqlservice.check.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorUnitTests {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorController authorController;


    @Test
    public void testGetAuthorById_ShouldReturnAuthor_WhenFound() throws Exception {
        Author author = new Author("author-1", "John", "Doe");
        when(authorRepository.findById("author-1")).thenReturn(Optional.of(author));

        Author result = authorController.getAuthorById("author-1");

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Doe");
        verify(authorRepository, times(1)).findById("author-1");
    }

    @Test
    public void testGetAuthorById_ShouldReturnNull_WhenTimeoutOccurs() {
        // Simulate a long delay for the repository call to trigger a timeout
        when(authorRepository.findById(anyString())).thenAnswer(invocation -> {
            TimeUnit.SECONDS.sleep(3); // Simulate a delay longer than 2 seconds
            return Optional.of(new Author("author-1", "John", "Doe"));
        });

        Author result = authorController.getAuthorById("author-1");

        assertThat(result).isNull(); // Expect null due to timeout
        verify(authorRepository, times(1)).findById("author-1");
    }
}
