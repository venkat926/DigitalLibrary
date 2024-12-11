package org.kvn.DigitalLibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.kvn.DigitalLibrary.enums.UserStatus;
import org.kvn.DigitalLibrary.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"USER\"")
@EqualsAndHashCode
public class User extends TimeStamps implements UserDetails {
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

    @JsonIgnore
    private String password;

    private String authorities;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // STUDENT, ADMIN ,
        String[] auth= authorities.split(",");
        return Arrays.stream(auth).map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
