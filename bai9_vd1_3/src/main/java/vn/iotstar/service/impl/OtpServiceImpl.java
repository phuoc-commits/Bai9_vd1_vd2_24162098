package vn.iotstar.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.entity.OtpToken;
import vn.iotstar.repository.OtpTokenRepository;
import vn.iotstar.service.EmailService;
import vn.iotstar.service.OtpService;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final int MAX_ATTEMPTS = 5;
    private final OtpTokenRepository repository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    private void send(String email, String type, String subject) {
        repository.deleteByEmailAndType(email, type);
        String otp = "%06d".formatted(random.nextInt(1_000_000));
        repository.save(OtpToken.builder().email(email).otpHash(encoder.encode(otp)).type(type)
                .expiresAt(LocalDateTime.now().plusMinutes(5)).createdAt(LocalDateTime.now()).build());
        emailService.sendOtp(email, otp, subject);
    }

    @Override @Transactional public void sendRegisterOtp(String email) { send(email, "REGISTER", "Xác nhận đăng ký"); }
    @Override @Transactional public void sendResetPasswordOtp(String email) { send(email, "RESET_PASSWORD", "Đặt lại mật khẩu"); }

    private boolean verify(String email, String otp, String type) {
        OtpToken token = repository.findTopByEmailAndTypeAndUsedFalseOrderByCreatedAtDesc(email, type).orElse(null);
        if (token == null || token.getExpiresAt().isBefore(LocalDateTime.now()) || token.getAttempts() >= MAX_ATTEMPTS) return false;
        token.setAttempts(token.getAttempts() + 1);
        boolean valid = encoder.matches(otp, token.getOtpHash());
        if (valid) token.setUsed(true);
        repository.save(token);
        return valid;
    }

    @Override @Transactional public boolean verifyRegisterOtp(String email, String otp) { return verify(email, otp, "REGISTER"); }
    @Override @Transactional public boolean verifyResetPasswordOtp(String email, String otp) { return verify(email, otp, "RESET_PASSWORD"); }
}