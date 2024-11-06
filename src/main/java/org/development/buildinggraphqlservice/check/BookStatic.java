package org.development.buildinggraphqlservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record BookStatic(String id, String name, int pageCount, String authorId) {

    private static List<BookStatic> books = new ArrayList<>(Arrays.asList(
            new BookStatic("book-1", "Effective Java", 416, "author-1"),
            new BookStatic("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2"),
            new BookStatic("book-3", "Down Under", 436, "author-3")
    ));

    public static BookStatic getById(String id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<BookStatic> getAllBooks() {
        return books.stream().toList();
    }

    public static BookStatic addBook(String name, Integer pageCount, String authorId) {
        BookStatic newBook = new BookStatic("book-" + (books.size() + 1), name, pageCount, authorId);
        books.add(newBook);
        return newBook;
    }
}
