package com.crud.democrud.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Size;

import com.crud.democrud.validator.DOBConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    private String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;

    private String firstName;
    private String lastName;

    @DOBConstraint(min = 18, message = "INVALID_DOB")
    private LocalDate dob;

    List<String> roles;
}
