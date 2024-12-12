package org.kvn.DigitalLibrary.service.impl;

import org.kvn.DigitalLibrary.dto.request.UserCreationRequest;
import org.kvn.DigitalLibrary.dto.response.UserCreationResponse;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.enums.UserFilter;
import org.kvn.DigitalLibrary.enums.UserType;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.repository.UserCacheRepository;
import org.kvn.DigitalLibrary.repository.UserRepository;
import org.kvn.DigitalLibrary.service.userFilter.UserFilterFactory;
import org.kvn.DigitalLibrary.service.userFilter.UserFilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  implements UserDetailsService {

    @Value("${student.authority}")
    private String studentAuthority;
    @Value("${admin.authority}")
    private String adminAuthority;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFilterFactory userFilterFactory;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserCacheRepository userCacheRepository;


    public UserCreationResponse addStudent(UserCreationRequest request) {
        User user = request.toUser();
        user.setUserType(UserType.STUDENT);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setAuthorities(studentAuthority);
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

    public UserCreationResponse addAdmin(UserCreationRequest request) {
        User user = request.toUser();
        user.setUserType(UserType.ADMIN);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setAuthorities(adminAuthority);
        User userFromDb = userRepository.save(user);
        return UserCreationResponse.builder().
                userName(userFromDb.getName()).
                userAddress(userFromDb.getAddress()).
                userEmail(userFromDb.getEmail()).
                userPhone(userFromDb.getPhoneNo()).
                build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userCacheRepository.getUser(email);
        if (user != null) {
            return user;
        }
        user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("No User Found");
        userCacheRepository.setUser(email, user);
        return user;
    }
}
