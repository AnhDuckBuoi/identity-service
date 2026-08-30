package com.crud.democrud.configuration;

import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crud.democrud.entity.Role;
import com.crud.democrud.entity.User;
import com.crud.democrud.repository.RoleRepository;
import com.crud.democrud.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Profile("!test")
    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                Role adminRole = roleRepository
                        .findById("ADMIN")
                        .orElseGet(() -> roleRepository.save(
                                Role.builder().name("ADMIN").description("Vua").build()));
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .firstName("System")
                        .lastName("admin")
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(user);

                log.warn("Admin has been created with password: admin");
            }
        };
    }
}
