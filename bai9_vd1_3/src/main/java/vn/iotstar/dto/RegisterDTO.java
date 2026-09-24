package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterDTO {
    @NotBlank @Size(min = 3, max = 30) private String username;
    @NotBlank @Email private String email;
    @NotBlank private String fullName;
    @NotBlank @Size(min = 6) private String password;
    @NotBlank private String confirmPassword;
}