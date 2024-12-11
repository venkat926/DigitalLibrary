package org.kvn.DigitalLibrary.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kvn.DigitalLibrary.dto.request.BookCreationRequest;
import org.kvn.DigitalLibrary.dto.response.BookCreationResponse;
import org.kvn.DigitalLibrary.enums.*;
import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.BookRepository;
import org.kvn.DigitalLibrary.service.bookFilter.BookFilterFactory;
import org.kvn.DigitalLibrary.service.bookFilter.BookFilterStrategy;
import org.kvn.DigitalLibrary.service.impl.AuthorService;
import org.kvn.DigitalLibrary.service.impl.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private BookFilterFactory bookFilterFactory;

    @Mock
    private BookFilterStrategy bookFilterStrategy;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        author = Author.builder().id("1").name("n1").email("n1@gmail.com").build();
        book = Book.builder().id(1).title("t1").bookNo("B1").bookType(BookType.EDUCATIONAL)
                .securityAmount(100).author(author).build();
        user = User.builder()
                .id(1).name("u1").phoneNo("123").email("u1@gmail.com")
                .address("add1").userStatus(UserStatus.ACTIVE).userType(UserType.STUDENT)
                .build();
    }

    @Test
    public void BookServiceTest_addBook_authorNull() {
        // Arrange
        BookCreationRequest request = BookCreationRequest.builder()
                .bookTitle("t1").bookNo("B1").bookType(BookType.EDUCATIONAL)
                .securityAmount(100).authorName("n1").authorEmail("n1@hgmail.com")
                .build();
        when(authorService.findAuthorInDb(any(String.class))).thenReturn(null);
        when(authorService.saveMyAuthor(any(Author.class))).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        BookCreationResponse response = bookService.addBook(request);

        // Assert
        Assertions.assertNotNull(response);
        verify(authorService, times(1)).findAuthorInDb(any(String.class));
        verify(authorService, times(1)).saveMyAuthor(any(Author.class));
        verify(bookRepository, times(1)).save(any(Book.class));
        Assertions.assertEquals(book.getTitle(), response.getBookName());
        Assertions.assertEquals(book.getBookNo(), response.getBookNo());
    }

    @Test
    public void BookServiceTest_addBook_authorNotNull() {
        // Arrange
        BookCreationRequest request = BookCreationRequest.builder()
                .bookTitle("t1").bookNo("B1").bookType(BookType.EDUCATIONAL)
                .securityAmount(100).authorName("n1").authorEmail("n1@hgmail.com")
                .build();
        when(authorService.findAuthorInDb(any(String.class))).thenReturn(author);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        BookCreationResponse response = bookService.addBook(request);

        // Assert
        Assertions.assertNotNull(response);
        verify(authorService, times(1)).findAuthorInDb(any(String.class));
        verify(bookRepository, times(1)).save(any(Book.class));
        Assertions.assertEquals(book.getTitle(), response.getBookName());
        Assertions.assertEquals(book.getBookNo(), response.getBookNo());
    }

    @Test
    public void BookServiceTest_filter() {
        // Arrange
        BookFilter filter = BookFilter.BOOK_TITLE;
        Operator operator = Operator.EQUALS;
        String value = "t1";
//        when(any(BookFilterStrategy.class).getFilteredBook(operator, value))
//                .thenReturn(Collections.singletonList(book));
        when(bookFilterFactory.getStrategy(filter)).thenReturn(bookFilterStrategy);
        when(bookFilterStrategy.getFilteredBook(operator, value)).thenReturn(Collections.singletonList(book));


        // Act
        List<Book> bookList = bookService.filter(filter, operator, value);

        // Assert
        Assertions.assertNotNull(bookList);
        verify(bookFilterFactory,times(1)).getStrategy(any(BookFilter.class));
        verify(bookFilterStrategy, times(1)).getFilteredBook(any(Operator.class), any(String.class));
        Assertions.assertEquals("B1", bookList.get(0).getBookNo());
        Assertions.assertEquals("n1@gmail.com", bookList.get(0).getAuthor().getEmail());
    }

    @Test
    public void BookServiceTest_checkIfBookIsValid_validBook() {
        // Arrange
        when(bookRepository.findByBookNo(any(String.class))).thenReturn(Collections.singletonList(book));

        // Act
        Book bookFromDb = bookService.checkIfBookIsValid("B1");

        // Assert
        Assertions.assertNotNull(bookFromDb);
    }

    @Test
    public void BookServiceTest_checkIfBookIsValid_InvalidBook() {
        // Arrange
        when(bookRepository.findByBookNo(any(String.class))).thenReturn(List.of());

        // Act
        Book bookFromDb = bookService.checkIfBookIsValid("B1");

        // Assert
        Assertions.assertNull(bookFromDb);
    }

    @Test
    public void BookServiceTest_markBookUnavailable() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        bookService.markBookUnavailable(book, user);

        // Assert
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
