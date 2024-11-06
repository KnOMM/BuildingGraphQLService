package org.development.buildinggraphqlservice.check;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Author")
@Getter
@Setter
@AllArgsConstructor
public class Author {
    @Id
    private String id;
    private String firstName;
    private String lastName;
}
