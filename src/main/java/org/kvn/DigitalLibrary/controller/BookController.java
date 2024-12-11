package org.kvn.DigitalLibrary.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.kvn.DigitalLibrary.dto.GenericReturnClass;
import org.kvn.DigitalLibrary.dto.request.BookCreationRequest;
import org.kvn.DigitalLibrary.dto.response.BookCreationResponse;
import org.kvn.DigitalLibrary.enums.BookFilter;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/addBook")
    public ResponseEntity<GenericReturnClass> addBook(@RequestBody @Validated BookCreationRequest request) {
        BookCreationResponse response = bookService.addBook(request);
        GenericReturnClass returnObject = GenericReturnClass.builder()
                .data(response)
                .build();
        if (response != null) {
            returnObject.setCode(200);
            returnObject.setMsg("Book Successfully added to the DB");
            return new ResponseEntity<>(returnObject, HttpStatus.OK);
        } else {
            returnObject.setCode(400);
            returnObject.setMsg("Failed to add book to DB");
            return new ResponseEntity<>(returnObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/filter")
    public ResponseEntity<GenericReturnClass> filterBooks(@NotNull(message = "filterBy must not be null") @RequestParam("filterBy") BookFilter bookFilter,
                                                          @NotNull(message = "operator must not be null") @RequestParam("operator")Operator operator,
                                                          @NotBlank(message = "value must not be blank") @RequestParam("value") String value) {
        System.out.println("Book Filter API called");
        List<Book> bookList = bookService.filter(bookFilter, operator, value);
        GenericReturnClass returnObject = GenericReturnClass.builder().data(bookList).build();
        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }
}
