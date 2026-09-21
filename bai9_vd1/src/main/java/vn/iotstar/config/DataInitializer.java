package vn.iotstar.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.*;
import vn.iotstar.entity.Role;
import vn.iotstar.repository.*;
@Configuration
public class DataInitializer {
 @Bean
 CommandLineRunner initData(RoleRepository roles, UserRepository users,
 PasswordEncoder
encoder,
 @Value("${ADMIN_EMAIL:phuoc2006@gmail.com}") String
adminEmail,
 @Value("${ADMIN_PASSWORD:123456}") String
adminPassword) {
 return args -> {
 Role userRole=roles.findByNameIgnoreCase("USER").orElseGet(() ->
roles.save(new Role("USER")));
 Role adminRole=roles.findByNameIgnoreCase("ADMIN").orElseGet(() ->
roles.save(new Role("ADMIN")));
 if (!users.existsByEmailIgnoreCase(adminEmail)) {
 User admin=new User();
 admin.setEmail(adminEmail.toLowerCase());
 admin.setFullName("Duong Minh Phuoc_24162098");
 admin.setPassword(encoder.encode(adminPassword));
 admin.setRole(adminRole);
 admin.setEnabled(true);
 users.save(admin);
 }

 };
 }
}