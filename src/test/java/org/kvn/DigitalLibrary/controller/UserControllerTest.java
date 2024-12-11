package org.kvn.DigitalLibrary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.kvn.DigitalLibrary.dto.request.UserCreationRequest;
import org.kvn.DigitalLibrary.dto.response.UserCreationResponse;
import org.kvn.DigitalLibrary.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void UserControllerTest_addStudent_Success() throws Exception {
        // Arrange
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                .userName("name1").userEmail("name1@gmail.com")
                .userAddress("address1").userPhone("987654")
                .build();

        UserCreationResponse userCreationResponse = UserCreationResponse.builder()
                .userName("name1").userEmail("name1@gmail.com")
                .userAddress("address1").userPhone("987654")
                .build();
        when(userService.addStudent(any(UserCreationRequest.class))).thenReturn(userCreationResponse);

        // Act
        ResultActions resultResponse = mockMvc.perform(post("/user/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreationRequest)));

        // Assert
        System.out.println(MockMvcResultMatchers.jsonPath("$.data"));
        resultResponse.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userEmail").value("name1@gmail.com"));


    }

    @Test
    public void UserControllerTest_filterUser() {

    }
}
