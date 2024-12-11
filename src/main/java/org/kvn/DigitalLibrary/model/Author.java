package org.kvn.DigitalLibrary.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(AuthorCompositeKey.class)
public class Author extends TimeStamps{
    @Id
    private String id;

    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(length = 50)
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> bookList;


}
