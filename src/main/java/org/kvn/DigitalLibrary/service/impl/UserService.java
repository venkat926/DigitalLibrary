package org.kvn.DigitalLibrary.service.impl;

import org.kvn.DigitalLibrary.dto.request.UserCreationRequest;
import org.kvn.DigitalLibrary.dto.response.UserCreationResponse;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.enums.UserFilter;
import org.kvn.DigitalLibrary.enums.UserType;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.UserRepository;
import org.kvn.DigitalLibrary.service.userFilter.UserFilterFactory;
import org.kvn.DigitalLibrary.service.userFilter.UserFilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFilterFactory userFilterFactory;

    public UserCreationResponse addStudent(UserCreationRequest request) {
        User user = request.toUser();
        user.setUserType(UserType.STUDENT);
        User userFromDb = userRepository.save(user);
        return UserCreationResponse.builder()
                .userName(userFromDb.getName())
                .userEmail(userFromDb.getEmail())
                .userAddress(userFromDb.getAddress())
                .userPhone(userFromDb.getPhoneNo())
                .build();
    }

    public List<User>  filter(UserFilter userFilter, Operator operator, String value) {
        UserFilterStrategy filterStrategy = userFilterFactory.getUserFilterStrategy(userFilter);
        return filterStrategy.getFilteredUsers(operator, value);
    }

    public User checkIfUserIsValid(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }
}
