package com.crud.democrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // General
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception.", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(1000, "Invalid key.", HttpStatus.BAD_REQUEST),

    // User
    USER_EXISTED(1001, "User already exists.", HttpStatus.BAD_REQUEST),

    USERNAME_INVALID(1002, "Username must be at least {min} characters.", HttpStatus.BAD_REQUEST),

    PASSWORD_INVALID(1003, "Password must be at least {min} characters.", HttpStatus.BAD_REQUEST),

    USER_NOT_EXISTED(1004, "User does not exist.", HttpStatus.NOT_FOUND),

    // Authentication
    WRONG_USERNAME_OR_PASSWORD(1005, "Username or password is incorrect.", HttpStatus.UNAUTHORIZED),

    // Authorization
    FORBIDDEN(1006, "You do not have permission to perform this action.", HttpStatus.FORBIDDEN),

    // Validation
    INVALID_REQUEST(1007, "Invalid request.", HttpStatus.BAD_REQUEST),
    //
    UNAUTHENTICATED(1008, "Authentication is required.", HttpStatus.UNAUTHORIZED),
    //
    PERMISSION_EXISTED(1009, "Permission already exists.", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1010, "Role already exists.", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1011, "Role does not exist.", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_EXISTED(1012, "Permission does not exist.", HttpStatus.NOT_FOUND),
    INVALID_DOB(1013, "Your age must be at least {min}.", HttpStatus.BAD_REQUEST),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
