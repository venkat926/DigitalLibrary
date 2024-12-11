package org.kvn.DigitalLibrary.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.kvn.DigitalLibrary.dto.GenericReturnClass;
import org.kvn.DigitalLibrary.dto.request.UserCreationRequest;
import org.kvn.DigitalLibrary.dto.response.UserCreationResponse;
import org.kvn.DigitalLibrary.enums.Operator;
import org.kvn.DigitalLibrary.enums.UserFilter;
import org.kvn.DigitalLibrary.model.User;
import org.kvn.DigitalLibrary.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/addStudent")
    public ResponseEntity<GenericReturnClass> addStudent(@RequestBody @Validated UserCreationRequest request) {
        UserCreationResponse response = userService.addStudent(request);
        GenericReturnClass returnObject = GenericReturnClass.builder()
                .data(response)
                .msg("User added Successfully")
                .build();
        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }

    @PostMapping("/addAdmin")
    public UserCreationResponse addAdmin(@RequestBody @Validated UserCreationRequest request) {
        return userService.addAdmin(request);
    }

    @GetMapping("/filter")
    public ResponseEntity<GenericReturnClass> filterUsers(@NotNull(message = "filterBy can not be NULL") @RequestParam("filterBy") UserFilter userFilter,
                                                          @NotNull(message = "Operator can not be NULL") @RequestParam("operator") Operator operator,
                                                          @NotBlank(message = "value can not be Blank") @RequestParam("value") String value) {
        List<User> userList = userService.filter(userFilter, operator, value);
        GenericReturnClass returnObject = GenericReturnClass.builder().data(userList).build();
        return new ResponseEntity<>(returnObject, HttpStatus.OK);
    }
}
