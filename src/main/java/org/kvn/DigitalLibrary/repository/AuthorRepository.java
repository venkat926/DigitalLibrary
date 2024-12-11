package org.kvn.DigitalLibrary.repository;

import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.model.AuthorCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, AuthorCompositeKey> {
    Author findByEmail(String authorEmail);
}
