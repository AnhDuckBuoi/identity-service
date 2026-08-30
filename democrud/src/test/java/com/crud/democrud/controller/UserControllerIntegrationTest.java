// package com.crud.democrud.controller;
//
// import com.crud.democrud.dto.request.UserCreationRequest;
// import com.crud.democrud.dto.response.UserResponse;
// import com.crud.democrud.service.UserService;
// import lombok.extern.slf4j.Slf4j;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentMatchers;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import org.testcontainers.containers.MySQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
// import tools.jackson.databind.ObjectMapper;
//
// import java.time.LocalDate;
//
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
// @Slf4j
// @SpringBootTest
// @AutoConfigureMockMvc
// @Testcontainers
//
// public class UserControllerIntegrationTest {
//    @Container
//    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
//    @DynamicPropertySource
//    static void configureDataSource(DynamicPropertyRegistry registry){
//        registry.add("spring.datasource.url",MY_SQL_CONTAINER::getJdbcUrl);
//        registry.add("spring.datasource.username",MY_SQL_CONTAINER::getUsername);
//        registry.add("spring.datasource.password",MY_SQL_CONTAINER::getPassword);
//        registry.add("spring.datasource.driverClassName",()->"com.mysql.cj.jdbc.Driver");
//        registry.add("spring.jpa.hibernate.ddl-auto",()->"update");
//    }
//    @Autowired
//    private MockMvc mockMvc;
//    @MockitoBean
//    private UserService userService;
//    private UserCreationRequest userCreationRequest;
//    private UserResponse userResponse;
//    @BeforeEach
//    void initData(){
//        userCreationRequest = UserCreationRequest.builder()
//                .username("duccm")
//                .password("12345678")
//                .firstName("Cao")
//                .lastName("ManhDuc")
//                .dob(LocalDate.of(2005, 6, 14))
//                .build();
//        userResponse = UserResponse.builder()
//                .id("8d55d560-7c00-4fcd-bc4e-49b3555c234b")
//                .username("duccm")
//                .firstName("Cao")
//                .lastName("ManhDuc")
//                .dob(LocalDate.of(2005, 6, 14))
//                .build();
//    }
//    @Test
//    void createUser_validRequest_success() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        String content = objectMapper.writeValueAsString(userCreationRequest);
//        Mockito.when(userService.createUser(ArgumentMatchers.any()))
//                .thenReturn(userResponse);
//        //WHEN, THEN
////        mockMvc.perform(MockMvcRequestBuilders
////                .post("/users")
////                .contentType(MediaType.APPLICATION_JSON_VALUE)
////                .content(content))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(jsonPath("$.code").value(1000))
////                .andExpect(jsonPath("$.res.username").value("duccm"))
////                .andExpect(jsonPath("$.res.firstName").value("Cao"))
////                .andExpect(jsonPath("$.res.lastName").value("ManhDuc"))
////                .andExpect(jsonPath("$.res.dob").value("2005-06-14"))
////                .andExpect(jsonPath("$.res.id").value("8d55d560-7c00-4fcd-bc4e-49b3555c234b"))
////                .andExpect(jsonPath("$.res.roles").isEmpty()
////                );
//    }
//    @Test
//    void createUser_invalidUsername_tooShort() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        userCreationRequest.setUsername("du");
//        String content = objectMapper.writeValueAsString(userCreationRequest);
//
//        //WHEN, THEN
////        mockMvc.perform(MockMvcRequestBuilders
////                        .post("/users")
////                        .contentType(MediaType.APPLICATION_JSON_VALUE)
////                        .content(content))
////                .andExpect(MockMvcResultMatchers.status().isBadRequest())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Username must be at least 3
// characters."))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1002)
////                );
//    }
//    @Test
//    void createUser_validPassword_tooShort() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        userCreationRequest.setPassword("1234");
//        String content = objectMapper.writeValueAsString(userCreationRequest);
//
//        //WHEN, THEN
////        mockMvc.perform(MockMvcRequestBuilders
////                        .post("/users")
////                        .contentType(MediaType.APPLICATION_JSON_VALUE)
////                        .content(content))
////                .andExpect(MockMvcResultMatchers.status().isBadRequest())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password must be at least 8
// characters."))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1003)
////                );
//    }
// }
