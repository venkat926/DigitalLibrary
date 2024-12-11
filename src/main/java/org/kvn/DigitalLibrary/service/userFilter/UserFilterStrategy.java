package org.kvn.DigitalLibrary.service.userFilter;

import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserFilterStrategy {
    public List<User> getFilteredUsers(Operator operator, String value);
}
