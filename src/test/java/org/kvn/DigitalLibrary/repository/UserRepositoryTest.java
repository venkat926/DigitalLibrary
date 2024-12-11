package org.kvn.DigitalLibrary.repository;

import org.aspectj.apache.bcel.classfile.Module;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.kvn.DigitalLibrary.enums.UserStatus;
import org.kvn.DigitalLibrary.enums.UserType;
import org.kvn.DigitalLibrary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeAll
    public void setup() {
        user1 = User.builder()
                .name("n1")
                .phoneNo("123")
                .email("n1@gmail.com")
                .address("add1")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.STUDENT)
                .build();
        user2 = User.builder()
                .name("n2")
                .phoneNo("456")
                .email("n2@gmail.com")
                .address("add2")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.ADMIN)
                .build();
        user3 = User.builder()
                .name("n3")
                .phoneNo("789")
                .email("n3@gmail.com")
                .address("add3")
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.STUDENT)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Test
    public void UserRepositoryTest_findByEmail() {
        // Arrange

        // Act
        User userFromDb = userRepository.findByEmail("n1@gmail.com");
        // Assert
        Assertions.assertNotNull(userFromDb);
        Assertions.assertEquals(user1.getName(), userFromDb.getName());
        Assertions.assertEquals(user1.getPhoneNo(), userFromDb.getPhoneNo());
    }

    @Test
    public void UserRepositoryTest_findByName() {
        // Arrange

        // Act
        List<User> userList = userRepository.findByName("n2");
        // Assert
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(user2.getEmail(), userList.get(0).getEmail());
        Assertions.assertEquals(user2.getPhoneNo(), userList.get(0).getPhoneNo());
    }

    @Test
    public void UserRepositoryTest_findByNameContaining() {
        // Arrange

        // Act
        List<User> userList = userRepository.findByNameContaining("n");
        // Assert
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(3, userList.size());
    }

    @Test
    public void UserRepositoryTest_findByPhoneNo() {
        // Arrange

        // Act
        User userFromDb = userRepository.findByPhoneNo("789");
        // Assert
        Assertions.assertNotNull(userFromDb);
        Assertions.assertEquals(user3.getName(),userFromDb.getName());
        Assertions.assertEquals(user3.getEmail(),userFromDb.getEmail());

    }

}
