package org.kvn.DigitalLibrary.repository;

import org.kvn.DigitalLibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String userEmail);

    List<User> findByName(String value);

    List<User> findByNameContaining(String value);

    User findByPhoneNo(String value);
}
