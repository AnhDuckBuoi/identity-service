package com.crud.democrud.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.crud.democrud.dto.request.UserCreationRequest;
import com.crud.democrud.dto.response.UserResponse;
import com.crud.democrud.service.UserService;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("/test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;

    @BeforeEach
    void initData() {
        userCreationRequest = UserCreationRequest.builder()
                .username("duccm")
                .password("12345678")
                .firstName("Cao")
                .lastName("ManhDuc")
                .dob(LocalDate.of(2005, 6, 14))
                .build();
        userResponse = UserResponse.builder()
                .id("8d55d560-7c00-4fcd-bc4e-49b3555c234b")
                .username("duccm")
                .firstName("Cao")
                .lastName("ManhDuc")
                .dob(LocalDate.of(2005, 6, 14))
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userCreationRequest);
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.res.username").value("duccm"))
                .andExpect(jsonPath("$.res.firstName").value("Cao"))
                .andExpect(jsonPath("$.res.lastName").value("ManhDuc"))
                .andExpect(jsonPath("$.res.dob").value("2005-06-14"))
                .andExpect(jsonPath("$.res.id").value("8d55d560-7c00-4fcd-bc4e-49b3555c234b"))
                .andExpect(jsonPath("$.res.roles").isEmpty());
    }

    @Test
    void createUser_invalidUsername_tooShort() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        userCreationRequest.setUsername("du");
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Username must be at least 3 characters."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1002));
    }

    @Test
    void createUser_validPassword_tooShort() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        userCreationRequest.setPassword("1234");
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password must be at least 8 characters."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1003));
    }
}
