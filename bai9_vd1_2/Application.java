@Bean
CommandLineRunner init(
 RoleRepository roleRepository,
 UserRepository userRepository,
 PasswordEncoder passwordEncoder
) {
 return args -> {
 Role userRole = roleRepository
 .findByName("ROLE_USER")
 .orElseGet(() ->
 roleRepository.save(
 Role.builder()
 .name("ROLE_USER")
 .build()
 )
 );
 if (userRepository
 .findByUsername("user01")
 .isEmpty()) {
 User user = User.builder()
 .username("user01")
 .email("user01@gmail.com")
 .password(
 passwordEncoder.encode("123456")
 )
.fullName("Nguyễn Hữu Trung")
 .images("/images/user.png")
 .role(userRole)
 .enabled(true)
 .build();
 userRepository.save(user);
 }
 };
}
