package org.kvn.DigitalLibrary.repository;


import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.kvn.DigitalLibrary.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

@DataJpaTest(properties = {
       "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorRepositoryTest {
    // Arrange -> Act -> Assert

    @Autowired
    private AuthorRepository authorRepository;

    private Author author1;
    private Author author2;
    private Author author3;


    @BeforeEach
    public  void setup() {
        author1 = Author.builder().id("1").email("a@gmail.com").name("a").build();
        author2 = Author.builder().id("2").email("b@gmail.com").name("b").build();
        author3 = Author.builder().id("3").email("c@gmail.com").name("c").build();
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
    }

    @Test
    public void AuthorRepositoryTest_findByEmail() {
        // Arrange

        //Act
        Author authorFromDB = authorRepository.findByEmail("a@gmail.com");
        // Assert
        Assertions.assertNotNull(authorFromDB, "Author should not be null");
        Assertions.assertEquals("a@gmail.com", authorFromDB.getEmail(), "authorEmail should match");
        Assertions.assertEquals("a", authorFromDB.getName(), "authorName should match");


    }
}
