package com.crud.democrud.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.crud.democrud.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    static final String[]
            PUBLIC_ENDPOINT = {"/users", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh"},
            PRIVITE_ENDPOINT = {"/roles/**", "/permissions/**"};

    @Autowired
    CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT)
                .permitAll()
                .requestMatchers(PRIVITE_ENDPOINT)
                .hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/users")
                .hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/users/**")
                .authenticated()
                .requestMatchers(HttpMethod.PUT, "/users/**")
                .authenticated()
                .anyRequest()
                .authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.decoder(customJwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
