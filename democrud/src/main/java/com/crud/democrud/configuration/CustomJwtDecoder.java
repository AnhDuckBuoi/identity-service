package com.crud.democrud.configuration;

import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.crud.democrud.dto.request.IntrospectRequest;
import com.crud.democrud.exception.AppException;
import com.crud.democrud.exception.ErrorCode;
import com.crud.democrud.service.AuthenticationService;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerkey}")
    String SIGNER_KEY;

    @Autowired
    @Lazy
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        var res = authenticationService.introspect(
                IntrospectRequest.builder().token(token).build());
        if (!res.isValid()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HmacSHA512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
