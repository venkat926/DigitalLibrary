package org.kvn.DigitalLibrary.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kvn.DigitalLibrary.enums.BookType;
import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Author author1;
    private Author author2;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    public void setup() {
        author1 = Author.builder().id("1").email("a@gmail.com").name("a").build();
        author2 = Author.builder().id("2").email("b@gmail.com").name("b").build();

        book1 = Book.builder()
                .title("t1")
                .bookNo("B1")
                .securityAmount(100)
                .bookType(BookType.EDUCATIONAL)
                .author(author1)
                .build();
        bookRepository.save(book1);

        book2 = Book.builder()
                .title("t2")
                .bookNo("B2")
                .securityAmount(200)
                .bookType(BookType.HISTORICAL)
                .author(author2)
                .build();
        bookRepository.save(book2);

        book3 = Book.builder()
                .title("t3")
                .bookNo("B3")
                .securityAmount(300)
                .bookType(BookType.EDUCATIONAL)
                .author(author2)
                .build();
        bookRepository.save(book3);
    }

    @Test
    public void BookRepositoryTest_findByBookNo() {
        // Arrange

        // Act
        List<Book> bookList = bookRepository.findByBookNo("B1");
        // Assert
        Assertions.assertEquals(1, bookList.size());
        Assertions.assertEquals(book1, bookList.get(0));
    }

    @Test
    public void BookRepositoryTest_findByTitle() {
        // Arrange

        // Act
        List<Book> bookList = bookRepository.findByTitle("t2");
        // Assert
        Assertions.assertEquals(1, bookList.size());
        Assertions.assertEquals(book2, bookList.get(0));
    }


    @Test
    public void BookRepositoryTest_findByBookType() {
        // Arrange

        // Act
        List<Book> bookList = bookRepository.findByBookType(BookType.EDUCATIONAL);
        // Assert
        Assertions.assertEquals(2, bookList.size());
        Assertions.assertEquals(book1, bookList.get(0));
        Assertions.assertEquals(book3, bookList.get(1));
    }

    @Test
    public void BookRepositoryTest_findByTitleContaining() {
        // Arrange

        // Act
        List<Book> bookList = bookRepository.findByTitleContaining("t");
        // Assert
        Assertions.assertEquals(3, bookList.size());
    }


}
