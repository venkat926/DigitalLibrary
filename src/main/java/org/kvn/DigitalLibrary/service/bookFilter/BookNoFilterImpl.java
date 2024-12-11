package org.kvn.DigitalLibrary.service.bookFilter;

import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class BookNoFilterImpl implements BookFilterStrategy{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getFilteredBook(Operator operator, String value) {
        if (!operator.equals(Operator.EQUALS)) {
            throw new IllegalArgumentException("Only Equals operator is expected with book No. for filtering");
        }
        return bookRepository.findByBookNo(value);
    }
}
