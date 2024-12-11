package org.kvn.DigitalLibrary.service.impl;

import org.kvn.DigitalLibrary.dto.request.BookCreationRequest;
import org.kvn.DigitalLibrary.dto.response.BookCreationResponse;
import org.kvn.DigitalLibrary.enums.BookFilter;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.BookRepository;
import org.kvn.DigitalLibrary.service.bookFilter.BookFilterFactory;
import org.kvn.DigitalLibrary.service.bookFilter.BookFilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookFilterFactory bookFilterFactory;

    public BookCreationResponse addBook(BookCreationRequest bookCreationRequest) {
        // check author is already present in DB or not
        Author authorFromDB = authorService.findAuthorInDb(bookCreationRequest.getAuthorEmail());
        if (authorFromDB == null) {
            authorFromDB = authorService.saveMyAuthor(Author.builder()
                            .id(UUID.randomUUID().toString())
                            .name(bookCreationRequest.getAuthorName())
                            .email(bookCreationRequest.getAuthorEmail())
                            .build());
        }
        // save book in DB
        Book book = bookCreationRequest.toBook();
        book.setAuthor(authorFromDB);
        book = bookRepository.save(book);
        // return response
        return BookCreationResponse.builder()
                .bookName(book.getTitle())
                .bookNo(book.getBookNo())
                .securityAmount(book.getSecurityAmount())
                .build();

    }

    public List<Book> filter(BookFilter bookFilter, Operator operator, String value) {
        BookFilterStrategy bookFilterStrategy = bookFilterFactory.getStrategy(bookFilter);
        return bookFilterStrategy.getFilteredBook(operator, value);
    }

    public Book checkIfBookIsValid(String bookNo) {
        List<Book> bookList = bookRepository.findByBookNo(bookNo);
        if (bookList.isEmpty()) return null;
        else return bookList.get(0);
    }

    public void markBookUnavailable(Book bookFromDb, User userFromDb) {
        bookFromDb.setUser(userFromDb);
        bookRepository.save(bookFromDb);
    }
}
