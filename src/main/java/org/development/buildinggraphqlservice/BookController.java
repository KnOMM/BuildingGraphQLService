package org.development.buildinggraphqlservice.check;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {
    @QueryMapping
    public BookStatic bookById(@Argument String id) {
        return BookStatic.getById(id);
    }

    @SchemaMapping
    public AuthorStatic author(BookStatic book) {
        return AuthorStatic.getById(book.authorId());
    }

    @QueryMapping
    public List<BookStatic> books() {
        return BookStatic.getAllBooks();
    }

    @MutationMapping(name="addBook")
    public BookStatic createBook(@Argument String name, @Argument int pageCount, @Argument String firstName, @Argument String lastName) {
        AuthorStatic newAuthor = AuthorStatic.addAuthor(firstName, lastName);
        return BookStatic.addBook(name, pageCount, newAuthor.id());
    }
}
