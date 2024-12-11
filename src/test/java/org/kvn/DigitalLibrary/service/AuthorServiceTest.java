package org.kvn.DigitalLibrary.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.repository.AuthorRepository;
import org.kvn.DigitalLibrary.service.impl.AuthorService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        author = Author.builder().id("1").name("n1").email("n1@gmail.com").build();
    }
    @Test
    public void AuthorServiceTest_saverMyAuthor() {
        // Arrange
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        // Act
        Author authorFromDb = authorService.saveMyAuthor(author);
        // Assert
        Assertions.assertNotNull(authorFromDb);
        Assertions.assertEquals("n1", authorFromDb.getName());
        Assertions.assertEquals("n1@gmail.com", authorFromDb.getEmail());
        verify(authorRepository, times(1)).save(any(Author.class));

    }

    @Test
    public void AuthorServiceTest_findAuthorInDb() {
        // Arrange
        when(authorRepository.findByEmail(any(String.class))).thenReturn(author);
        // Act
        Author authorFromDb = authorService.findAuthorInDb("n1@gmail.com");
        // Assert
        Assertions.assertNotNull(authorFromDb);
        Assertions.assertEquals("n1", authorFromDb.getName());
        Assertions.assertEquals("n1@gmail.com", authorFromDb.getEmail());
        verify(authorRepository, times(1)).findByEmail(any(String.class));

    }

}
