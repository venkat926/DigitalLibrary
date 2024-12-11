package org.kvn.DigitalLibrary.service.userFilter;

import org.kvn.DigitalLibrary.enums.UserFilter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserFilterFactory {

    public static final Map<UserFilter, UserFilterStrategy> filterStrategyMap= new HashMap<>();

    public UserFilterFactory(UserNameFilterImpl userNameFilter, UserEmailFilterImpl userEmailFilter, UserPhoneNoFilterImpl userPhoneNoFilter) {
        filterStrategyMap.put(UserFilter.NAME, userNameFilter);
        filterStrategyMap.put(UserFilter.EMAIL, userEmailFilter);
        filterStrategyMap.put(UserFilter.PHONE_NO, userPhoneNoFilter);
    }

    public UserFilterStrategy getUserFilterStrategy(UserFilter userFilter) {
        return filterStrategyMap.get(userFilter);
    }
}
