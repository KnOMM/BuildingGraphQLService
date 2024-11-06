package org.development.buildinggraphqlservice;

import org.development.buildinggraphqlservice.check.Author;
import org.development.buildinggraphqlservice.check.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.concurrent.*;

@Controller("/author")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @QueryMapping(name = "authorById")
    public Author getAuthorById(@Argument String id) {
        try {
            return CompletableFuture.supplyAsync(() -> authorRepository.findById(id).orElse(null), executor)
                    .get(2, TimeUnit.SECONDS); // Set timeout of 2 seconds
        } catch (TimeoutException e) {
            // Log timeout and return null if the repository call takes longer than 2 seconds
            System.out.println("Timeout: AuthorRepository did not respond in time.");
            return null;
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace();
            return null;
        }
    }
}
