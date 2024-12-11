package org.kvn.DigitalLibrary.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kvn.DigitalLibrary.dto.request.BookCreationRequest;
import org.kvn.DigitalLibrary.dto.response.BookCreationResponse;
import org.kvn.DigitalLibrary.enums.BookFilter;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.service.impl.BookService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void BookControllerTest_addBook_Success() throws Exception {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bookTitle","bookTitle");
        jsonObject.put("bookNo","B1");
        jsonObject.put("bookType","EDUCATIONAL");
        jsonObject.put("securityAmount",100.0);
        jsonObject.put("authorName","authorName");
        jsonObject.put("authorEmail","author@gmail.com");

        BookCreationResponse response = BookCreationResponse.builder()
                .bookName("bookTitle").bookNo("B1").securityAmount(100.0)
                .build();

        when(bookService.addBook(any(BookCreationRequest.class))).thenReturn(response);

        // Act
        ResultActions resultResponse = mockMvc.perform(post("/book/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()));

        // Assert
        resultResponse.andExpect(MockMvcResultMatchers.status().isOk());
        verify(bookService, times(1)).addBook(any(BookCreationRequest.class));
    }
    @Test
    public void BookControllerTest_addBook_Failure() throws Exception {
        // Arrange
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bookTitle","bookTitle");
        jsonObject.put("bookNo","B1");
        jsonObject.put("bookType","EDUCATIONAL");
        jsonObject.put("securityAmount",100.0);
        jsonObject.put("authorName","authorName");
        jsonObject.put("authorEmail","author@gmail.com");

        when(bookService.addBook(any(BookCreationRequest.class))).thenReturn(null);

        // Act
        ResultActions resultResponse = mockMvc.perform(post("/book/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()));

        // Assert
        resultResponse.andExpect(MockMvcResultMatchers.status().isInternalServerError());
        verify(bookService, times(1)).addBook(any(BookCreationRequest.class));
    }

    @Test
    public void BookControllerTest_filterBooks() throws Exception {
        // Arrange
        List<Book> mockBooks = List.of(
                Book.builder().title("t1").bookNo("B1").securityAmount(100).build()
        );
        when(bookService.filter(any(BookFilter.class), any(Operator.class), any(String.class))).thenReturn(mockBooks);


        // Act
        ResultActions response = mockMvc.perform(get("/book/filter")
                .param("filterBy", "BOOK_TITLE")
                .param("operator", "EQUALS")
                .param("value", "t1"));

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("t1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].bookNo").value("B1"));
    }
}
