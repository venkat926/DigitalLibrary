package org.kvn.DigitalLibrary.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kvn.DigitalLibrary.dto.request.UserCreationRequest;
import org.kvn.DigitalLibrary.dto.response.UserCreationResponse;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.enums.UserFilter;
import org.kvn.DigitalLibrary.enums.UserStatus;
import org.kvn.DigitalLibrary.enums.UserType;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.UserRepository;
import org.kvn.DigitalLibrary.service.impl.AuthorService;
import org.kvn.DigitalLibrary.service.impl.UserService;
import org.kvn.DigitalLibrary.service.userFilter.UserFilterFactory;
import org.kvn.DigitalLibrary.service.userFilter.UserFilterStrategy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFilterFactory userFilterFactory;

    @Mock
    private UserFilterStrategy userFilterStrategy;

    @InjectMocks
    private UserService userService;

    private User user;

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
    }

    @Test
    public void UserServiceTest_addStudent() {
        // Arrange
        UserCreationRequest request = UserCreationRequest.builder()
                .userName("n1")
                .userEmail("n1@gmail.com")
                .userAddress("add1")
                .userPhone("123")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);
        // Act
        UserCreationResponse response =  userService.addStudent(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("n1@gmail.com", response.getUserEmail());
    }

    @Test
    public void UserServiceTest_filter() {
        // Arrange
        UserFilter filter = UserFilter.NAME;
        Operator operator = Operator.EQUALS;
        String value = "n1";
        when(userFilterFactory.getUserFilterStrategy(filter)).thenReturn(userFilterStrategy);
        when(userFilterStrategy.getFilteredUsers(operator, value)).thenReturn(List.of(user));

        // Act
        List<User> userList = userService.filter(filter, operator, value);

        // Assert
        Assertions.assertNotNull(userList);
        Assertions.assertEquals("n1@gmail.com", userList.get(0).getEmail());
        verify(userFilterFactory, times(1)).getUserFilterStrategy(filter);
        verify(userFilterStrategy, times(1)).getFilteredUsers(operator, value);

    }

    @Test
    public void UserServiceTest_checkIfUserIsValid() {
        // Arrange

       when(userRepository.findByEmail("n1@gmail.com")).thenReturn(user);

        // Act
        User userFromDb = userService.checkIfUserIsValid("n1@gmail.com");

        // Assert
        Assertions.assertNotNull(userFromDb);
        Assertions.assertEquals("n1", userFromDb.getName());
        verify(userRepository, times(1)).findByEmail(any(String.class));
    }

}
