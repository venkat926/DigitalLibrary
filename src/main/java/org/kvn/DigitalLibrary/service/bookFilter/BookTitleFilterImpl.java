package org.kvn.DigitalLibrary.service.bookFilter;

import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class BookTitleFilterImpl implements BookFilterStrategy{
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getFilteredBook(Operator operator, String value) {
       if (operator.equals(Operator.EQUALS)) {
           return bookRepository.findByTitle(value);
       } else if (operator.equals(Operator.LIKE)) {
           return bookRepository.findByTitleContaining(value);
       } else {
           throw new IllegalArgumentException("EQUALS ans LIKE Operators are expected for filtering with title");
       }
    }
}
