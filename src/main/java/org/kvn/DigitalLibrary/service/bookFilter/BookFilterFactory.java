package org.kvn.DigitalLibrary.service.bookFilter;

import org.kvn.DigitalLibrary.enums.BookFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookFilterFactory {
    private  final Map<BookFilter, BookFilterStrategy> strategies = new HashMap<>();

    public BookFilterFactory(BookTitleFilterImpl bookTitleFilter, BookNoFilterImpl bookNoFilter, BookTypeFilterImpl bookTypeFilter) {
        strategies.put(BookFilter.BOOK_NO, bookNoFilter);
        strategies.put(BookFilter.BOOK_TITLE, bookTitleFilter);
        strategies.put(BookFilter.BOOK_TYPE, bookTypeFilter);
    }

    public BookFilterStrategy getStrategy(BookFilter bookFilter) {
        return strategies.get(bookFilter);
    }

}
