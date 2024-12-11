package org.kvn.DigitalLibrary.service.userFilter;

import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserNameFilterImpl implements UserFilterStrategy {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getFilteredUsers(Operator operator, String value) {
        if (operator.equals(Operator.EQUALS)) {
            return userRepository.findByName(value);
        } else if (operator.equals(Operator.LIKE)) {
            return userRepository.findByNameContaining(value);
        } else {
            throw new IllegalArgumentException("Only EQUALS and LIKE operators are supported with user name");
        }
    }
}
