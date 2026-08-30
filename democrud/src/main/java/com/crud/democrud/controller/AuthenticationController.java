package com.crud.democrud.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.democrud.dto.request.*;
import com.crud.democrud.dto.response.AuthenticationResponse;
import com.crud.democrud.dto.response.IntrospectResponse;
import com.crud.democrud.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("token")
    ApiResponse<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request) {
        var res = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder().res(res).build();
    }

    @PostMapping("introspect")
    ApiResponse<IntrospectResponse> authentication(@RequestBody IntrospectRequest request) {
        var res = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder().res(res).build();
    }

    @PostMapping("logout")
    ApiResponse<Void> authentication(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshTokentRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .res(authenticationService.refreshToken(request))
                .message("JWT has been refreshed")
                .build();
    }
}
