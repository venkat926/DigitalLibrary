package org.kvn.DigitalLibrary.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kvn.DigitalLibrary.enums.BookType;
import org.kvn.DigitalLibrary.enums.TxnStatus;
import org.kvn.DigitalLibrary.enums.UserStatus;
import org.kvn.DigitalLibrary.enums.UserType;
import org.kvn.DigitalLibrary.model.Author;
import org.kvn.DigitalLibrary.model.Book;
import org.kvn.DigitalLibrary.model.Txn;
import org.kvn.DigitalLibrary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class TxnRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TxnRepository txnRepository;

    private User user1;
    private User user2;

    private Book book1;
    private Book book2;

    private Txn txn1;
    private Txn txn2;

    @BeforeEach
    public void setup() {
        user1 = User.builder().name("n1").phoneNo("12345").email("n1@gmail.com").address("add1").userStatus(UserStatus.ACTIVE).userType(UserType.STUDENT).build();
        user2 = User.builder().name("n2").phoneNo("45678").email("n2@gmail.com").address("add2").userStatus(UserStatus.ACTIVE).userType(UserType.ADMIN).build();
        userRepository.save(user1);
        userRepository.save(user2);

        Author author1 = Author.builder().id("1").email("a@gmail.com").name("a").build();
        Author author2 = Author.builder().id("2").email("b@gmail.com").name("b").build();

        book1 = Book.builder().title("t1").bookNo("B1").securityAmount(100).bookType(BookType.EDUCATIONAL).author(author1).build();
        book2 = Book.builder().title("t2").bookNo("B2").securityAmount(200).bookType(BookType.HISTORICAL).author(author2).build();
        bookRepository.save(book1);
        bookRepository.save(book2);

        txn1 = Txn.builder()
                .txnId("1")
                .user(user1)
                .book(book1)
                .txnStatus(TxnStatus.ISSUED)
                .issuedDate(new Date())
                .build();
        txn2 = Txn.builder()
                .txnId("2")
                .user(user2)
                .book(book2)
                .txnStatus(TxnStatus.ISSUED)
                .issuedDate(new Date())
                .build();

        txnRepository.save(txn1);
        txnRepository.save(txn2);
    }

    @Test
    public void TxnRepositoryTest_findByTxnId() {
        // Arrange

        // Act
        Txn txnFromDb = txnRepository.findByTxnId("1");
        // Assert
        Assertions.assertNotNull(txnFromDb);
        Assertions.assertEquals("t1", txnFromDb.getBook().getTitle());
        Assertions.assertEquals("n1", txnFromDb.getUser().getName());
    }
}
