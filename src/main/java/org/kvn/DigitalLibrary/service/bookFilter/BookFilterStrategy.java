package org.kvn.DigitalLibrary.service.bookFilter;

import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookFilterStrategy {
    List<Book> getFilteredBook(Operator operator, String value) ;
}
