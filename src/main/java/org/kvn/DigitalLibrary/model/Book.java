package org.kvn.DigitalLibrary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.kvn.DigitalLibrary.enums.BookType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String title;

    @Column(length = 20, unique = true)
    private String bookNo;

    @Enumerated
    private BookType bookType;

    @Column(nullable = false)
    private double securityAmount;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "author_id", referencedColumnName = "id"),
            @JoinColumn(name = "author_email", referencedColumnName = "email")
            })
    @JsonIgnoreProperties(value = {"bookList"})
    private Author author;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties(value = {"user", "book"})
    private List<Txn> txnList;


}
