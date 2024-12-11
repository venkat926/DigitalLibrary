package org.kvn.DigitalLibrary.service.impl;

import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author saveMyAuthor(Author author) {
        return authorRepository.save(author);
    }
    public Author findAuthorInDb(String authorEmail) {
        return authorRepository.findByEmail(authorEmail);
    }
}
