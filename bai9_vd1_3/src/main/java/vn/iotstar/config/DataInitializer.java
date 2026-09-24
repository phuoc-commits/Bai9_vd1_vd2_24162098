package vn.iotstar.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(RoleRepository roles, UserRepository users,
            PasswordEncoder encoder) {
        return args -> {
                Role userRole = roles.findByName("USER").orElseGet(() -> roles.save(Role.builder().name("USER").build()));
                Role adminRole = roles.findByName("ADMIN").orElseGet(() -> roles.save(Role.builder().name("ADMIN").build()));
                if (!users.existsByUsername("admin")) users.save(User.builder().username("admin").email("admin@example.com")
                    .password(encoder.encode("123456")).fullName("System Administrator")
                    .role(adminRole).build());
                if (!users.existsByUsername("user")) users.save(User.builder().username("user").email("user@example.com")
                    .password(encoder.encode("123456")).fullName("Nguyen Van User")
                    .role(userRole).build());
        };
    }
}