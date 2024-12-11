package org.kvn.DigitalLibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.kvn.DigitalLibrary.enums.UserStatus;
import org.kvn.DigitalLibrary.enums.UserType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"USER\"")
@EqualsAndHashCode
public class User extends TimeStamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(unique = true, length = 15)
    private String phoneNo;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    private String address;

    @Enumerated
    private UserStatus userStatus;

    @Enumerated
    private UserType userType;

    @Transient
    private String temp;



    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"user", "author", "txnList"})
    List<Book> bookList;


    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"user", "book"})
    List<Txn> txnList;
}
