package org.kvn.DigitalLibrary.service.userFilter;

import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserEmailFilterImpl implements UserFilterStrategy{

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getFilteredUsers(Operator operator, String value) {
        if (operator.equals(Operator.EQUALS)) {
            User user = userRepository.findByEmail(value);
            return Collections.singletonList(user);
        } else {
            throw new IllegalArgumentException("Only EQUALS operation is allowed for email");
        }
    }
}
