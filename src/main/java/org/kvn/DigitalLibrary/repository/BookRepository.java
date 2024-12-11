package org.kvn.DigitalLibrary.repository;

import org.kvn.DigitalLibrary.enums.BookType;
import org.kvn.DigitalLibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByBookNo(String bookNo);

    List<Book> findByTitle(String value);

    List<Book> findByTitleContaining(String value);

    List<Book> findByBookType(BookType value);
}
