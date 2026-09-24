package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.dto.RegisterDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.AuthService;
import vn.iotstar.service.OtpService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final OtpService otpService;

    @Override @Transactional
    public void register(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) throw new IllegalArgumentException("Mật khẩu xác nhận không đúng.");
        if (users.existsByUsername(dto.getUsername()) || users.existsByEmail(dto.getEmail())) throw new IllegalArgumentException("Username hoặc email đã tồn tại.");
        Role role = roles.findByName("USER").orElseGet(() -> roles.save(Role.builder().name("USER").build()));
        users.save(User.builder().username(dto.getUsername()).email(dto.getEmail()).fullName(dto.getFullName())
                .password(encoder.encode(dto.getPassword())).role(role).enabled(false).build());
        otpService.sendRegisterOtp(dto.getEmail());
    }

    @Override @Transactional
    public boolean verifyRegister(String email, String otp) {
        if (!otpService.verifyRegisterOtp(email, otp)) return false;
        User user = users.findByEmail(email).orElseThrow(); user.setEnabled(true); return true;
    }
    @Override public void forgotPassword(String email) {
        if (users.findByEmail(email).isEmpty()) throw new IllegalArgumentException("Email chưa được đăng ký.");
        otpService.sendResetPasswordOtp(email);
    }
    @Override public boolean verifyResetOtp(String email, String otp) { return otpService.verifyResetPasswordOtp(email, otp); }
    @Override @Transactional public void resetPassword(String email, String password) {
        users.findByEmail(email).orElseThrow().setPassword(encoder.encode(password));
    }
}