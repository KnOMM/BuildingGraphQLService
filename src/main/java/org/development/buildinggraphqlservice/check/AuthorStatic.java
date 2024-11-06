package org.development.buildinggraphqlservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record AuthorStatic(String id, String firstName, String lastName) {

    private static List<AuthorStatic> authors = new ArrayList<>(Arrays.asList(
            new AuthorStatic("author-1", "Joshua", "Bloch"),
            new AuthorStatic("author-2", "Douglas", "Adams"),
            new AuthorStatic("author-3", "Bill", "Bryson")
    ));

    public static AuthorStatic getById(String id) {
        return authors.stream()
                .filter(author -> author.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static AuthorStatic addAuthor(String firstName, String lastName) {
        AuthorStatic newAuthor = new AuthorStatic("author-" + (authors.size() + 1), firstName, lastName);
        authors.add(newAuthor);
        return newAuthor;
    }
}
