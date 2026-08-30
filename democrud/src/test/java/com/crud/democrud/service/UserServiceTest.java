package com.crud.democrud.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.crud.democrud.dto.request.UserCreationRequest;
import com.crud.democrud.entity.User;
import com.crud.democrud.exception.AppException;
import com.crud.democrud.exception.ErrorCode;
import com.crud.democrud.repository.RoleRepository;
import com.crud.democrud.repository.UserRepository;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    private UserCreationRequest userCreationRequest;
    private User user;

    @BeforeEach
    void initData() {

        userCreationRequest = UserCreationRequest.builder()
                .username("duccm")
                .password("12345678")
                .firstName("Cao")
                .lastName("ManhDuc")
                .dob(LocalDate.of(2005, 6, 14))
                .roles(List.of("USER"))
                .build();

        user = User.builder()
                .id("8d55d560-7c00-4fcd-bc4e-49b3555c234b")
                .username("duccm")
                .password("12345678")
                .firstName("Cao")
                .lastName("ManhDuc")
                .dob(LocalDate.of(2005, 6, 14))
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);

        Mockito.when(roleRepository.findAllById(any())).thenReturn(List.of());

        Mockito.when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId("8d55d560-7c00-4fcd-bc4e-49b3555c234b");
            return u;
        });
        // WHEN
        var response = userService.createUser(userCreationRequest);
        // THEN
        Assertions.assertEquals("8d55d560-7c00-4fcd-bc4e-49b3555c234b", response.getId());
        Assertions.assertEquals("duccm", response.getUsername());
    }

    @Test
    void createUser_invalidRequest_fail() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = Assertions.assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));
        // THEN
        Assertions.assertEquals(
                ErrorCode.USER_EXISTED.getCode(), exception.getErrorCode().getCode());
    }
}
