package com.crud.democrud.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.crud.democrud.validator.DOBConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdationRequest {
    String password;
    String firstName;
    String lastName;

    @DOBConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}
