package org.kvn.DigitalLibrary.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kvn.DigitalLibrary.dto.request.TxnRequest;
import org.kvn.DigitalLibrary.dto.request.TxnReturnRequest;
import org.kvn.DigitalLibrary.enums.BookType;
import org.kvn.DigitalLibrary.enums.TxnStatus;
import org.kvn.DigitalLibrary.enums.UserStatus;
import org.kvn.DigitalLibrary.enums.UserType;
import org.kvn.DigitalLibrary.exception.BookException;
import org.kvn.DigitalLibrary.exception.TxnException;
import org.kvn.DigitalLibrary.exception.UserException;
import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.model.Txn;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.TxnRepository;
import org.kvn.DigitalLibrary.service.impl.BookService;
import org.kvn.DigitalLibrary.service.impl.TxnService;
import org.kvn.DigitalLibrary.service.impl.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TxnServiceTest {

    @Mock
    private TxnRepository txnRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private TxnService txnService;

    private User user;
    private Author author;
    private Book book;
    private Txn txn;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1)
                .name("n1")
                .phoneNo("123")
                .email("n1@gmail.com")
                .address("add1")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.STUDENT)
                .build();

        author = Author.builder().id("1").name("a1").email("a1@gmail.com").build();
        book = Book.builder().id(1).title("t1").bookNo("B1").bookType(BookType.EDUCATIONAL)
                .securityAmount(100).author(author).build();

        txn = Txn.builder()
                .txnId("1234-5678")
                .user(user)
                .book(book)
                .txnStatus(TxnStatus.ISSUED)
                .issuedDate(new Date())
                .build();
    }

    @Test
    public void TxnServiceTest_create_UserNotValid() {
        // Arrange
        TxnRequest request = TxnRequest.builder().userEmail("u1@gmail.com").bookNo("B1").build();
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(null);

        // Act
        UserException exception = Assertions.assertThrows(UserException.class, ()->{
            txnService.create(request);
        });

        // Assert
        Assertions.assertEquals("User is not valid", exception.getMessage());
    }

    @Test
    public void TxnServiceTest_create_BookNotValid() {
        // Arrange
        TxnRequest request = TxnRequest.builder().userEmail("u1@gmail.com").bookNo("B1").build();
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(null);


        // Act
        BookException exception = Assertions.assertThrows(BookException.class, ()->{
            txnService.create(request);
        });

        // Assert
        Assertions.assertEquals("Book id not valid", exception.getMessage());
    }

    @Test
    public void TxnServiceTest_create_BookNotAvailable() {
        // Arrange
        TxnRequest request = TxnRequest.builder().userEmail("u1@gmail.com").bookNo("B1").build();
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(book);
        book.setUser(user);

        // Act
        BookException exception = Assertions.assertThrows(BookException.class, ()->{
            txnService.create(request);
        });

        // Assert
        Assertions.assertEquals("Book is not available to issue", exception.getMessage());
    }

    @Test
    public void TxnServiceTest_create_Success() throws BookException, UserException {
        // Arrange
        TxnRequest request = TxnRequest.builder().userEmail("u1@gmail.com").bookNo("B1").build();
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(book);
        when(txnRepository.save(any(Txn.class))).thenReturn(txn);
        doNothing().when(bookService).markBookUnavailable(book, user);

        // Act
        String txnId = txnService.create(request);

        // Assert
        Assertions.assertNotNull(txnId);
        Assertions.assertEquals(36, txnId.length());
    }

    @Test
    public void TxnServiceTest_returnBookTxn_UserNotValid() {
        // Arrange
        TxnReturnRequest request = TxnReturnRequest.builder().userEmail("u1@gmail.com")
                .txnId("1234-5678").bookNo("B1").build();
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(null);

        // Act
        UserException exception = Assertions.assertThrows(UserException.class, ()->{
            txnService.returnBookTxn(request);
        });

        // Assert
        Assertions.assertEquals("User is not valid", exception.getMessage());
    }

    @Test
    public void TxnServiceTest_returnBookTxn_BookNotValid() {
        // Arrange
        TxnReturnRequest request = TxnReturnRequest.builder().userEmail("u1@gmail.com")
                .txnId("1234-5678").bookNo("B1").build();
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(null);


        // Act
        BookException exception = Assertions.assertThrows(BookException.class, ()->{
            txnService.returnBookTxn(request);
        });

        // Assert
        Assertions.assertEquals("Book id not valid", exception.getMessage());
    }

    @Test
    public void TxnServiceTest_returnBookTxn_BookNotAssigned() {
        // Arrange
        TxnReturnRequest request = TxnReturnRequest.builder().userEmail("u1@gmail.com")
                       .txnId("1234-5678").bookNo("B1").build();
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(book);

        // Act
        TxnException exception = Assertions.assertThrows(TxnException.class, ()->{
            txnService.returnBookTxn(request);
        });

        // Assert
        Assertions.assertEquals("Book is assigned to someone else, or not at all assigned", exception.getMessage());
    }


    @Test
    public void TxnServiceTest_returnBookTxn_TxnNull() throws BookException, UserException {
        // Arrange
        TxnReturnRequest request = TxnReturnRequest.builder().userEmail("u1@gmail.com")
                .txnId("1234-5678").bookNo("B1").build();
        book.setUser(user);
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(book);
        when(txnRepository.findByTxnId(request.getTxnId())).thenReturn(null);


        // Act
        TxnException exception = Assertions.assertThrows(TxnException.class, ()->{
            txnService.returnBookTxn(request);
        });


        // Assert
        Assertions.assertEquals("No txn has been found in Db with this txnID", exception.getMessage());


    }

    @Test
    public void TxnServiceTest_returnBookTxn_Success_StatusReturned() throws BookException, UserException {
        // Arrange
        TxnReturnRequest request = TxnReturnRequest.builder().userEmail("u1@gmail.com")
                .txnId("1234-5678").bookNo("B1").build();
        book.setUser(user);
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(book);
        when(txnRepository.findByTxnId(request.getTxnId())).thenReturn(txn);
        ReflectionTestUtils.setField(txnService, "validUpto", 10);
        ReflectionTestUtils.setField(txnService, "finePerDay", 1);
        when(txnRepository.save(txn)).thenReturn(txn);

        // Act
        double amount = txnService.returnBookTxn(request);

        // Assert
    }

    @Test
    public void TxnServiceTest_returnBookTxn_Success_StatusFined() throws BookException, UserException, ParseException {
        // Arrange
        setCustomDateForTxn(txn);
        TxnReturnRequest request = TxnReturnRequest.builder().userEmail("u1@gmail.com")
                .txnId("1234-5678").bookNo("B1").build();
        book.setUser(user);
        when(userService.checkIfUserIsValid(request.getUserEmail())).thenReturn(user);
        when(bookService.checkIfBookIsValid(request.getBookNo())).thenReturn(book);
        when(txnRepository.findByTxnId(request.getTxnId())).thenReturn(txn);
        ReflectionTestUtils.setField(txnService, "validUpto", 10);
        ReflectionTestUtils.setField(txnService, "finePerDay", 100);
        when(txnRepository.save(txn)).thenReturn(txn);

        // Act
        double amount = txnService.returnBookTxn(request);

        // Assert
    }

    private void setCustomDateForTxn(Txn txn) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date customDate = formatter.parse("2023-01-01");
        txn.setIssuedDate(customDate);
    }

}
