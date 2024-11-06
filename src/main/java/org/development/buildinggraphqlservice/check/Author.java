package org.development.buildinggraphqlservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@Getter
@Setter
public class Author {
    private String id;
    private String firstName;
    private String lastName;
}
