package com.crud.democrud.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.crud.democrud.dto.request.AuthenticationRequest;
import com.crud.democrud.dto.request.IntrospectRequest;
import com.crud.democrud.dto.request.LogoutRequest;
import com.crud.democrud.dto.request.RefreshTokentRequest;
import com.crud.democrud.dto.response.AuthenticationResponse;
import com.crud.democrud.dto.response.IntrospectResponse;
import com.crud.democrud.entity.InvalidatedToken;
import com.crud.democrud.entity.User;
import com.crud.democrud.exception.AppException;
import com.crud.democrud.exception.ErrorCode;
import com.crud.democrud.repository.InvalidatedTokenRepository;
import com.crud.democrud.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerkey}")
    protected String SIGNER_KEY;

    PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.WRONG_USERNAME_OR_PASSWORD);
        }
        String tkn = generateToke(user);
        return AuthenticationResponse.builder()
                .token(tkn)
                .isAuthenticated(authenticated)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        try {
            verifyToken(request.getToken());

            return IntrospectResponse.builder().valid(true).build();

        } catch (Exception e) {
            return IntrospectResponse.builder().valid(false).build();
        }
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signedToken = verifyToken(request.getToken());
        String jti = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private String generateToke(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        StringJoiner stringJoiner = new StringJoiner(" ");
        user.getRoles().forEach(role -> {
            stringJoiner.add("ROLE_" + role.getName());
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions().forEach(permission -> {
                    stringJoiner.add(permission.getName());
                });
            }
        });
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("caomanhduc.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", stringJoiner.toString())
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    public AuthenticationResponse refreshToken(RefreshTokentRequest request) throws ParseException, JOSEException {
        var signedJwt = verifyToken(request.getToken());
        String jti = signedJwt.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJwt.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build();
        invalidatedTokenRepository.save(invalidatedToken);
        String userId = signedJwt.getJWTClaimsSet().getSubject();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        return AuthenticationResponse.builder()
                .token(generateToke(user))
                .isAuthenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var ok = signedJWT.verify(verifier);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!(ok && expiryTime.after(new Date()))
                || invalidatedTokenRepository.existsById(
                        signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
}
