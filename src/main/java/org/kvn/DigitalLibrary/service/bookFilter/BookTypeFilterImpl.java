package org.kvn.DigitalLibrary.service.bookFilter;

import org.kvn.DigitalLibrary.enums.BookType;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookTypeFilterImpl implements BookFilterStrategy{
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getFilteredBook(Operator operator, String value) {
        if (operator.equals(Operator.EQUALS)) {
            return bookRepository.findByBookType(BookType.valueOf(value));
        } else {
            throw new IllegalArgumentException("Only EQUALS Operator is expected for filtering with BookType");
        }
    }
}
