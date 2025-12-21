package app.esiroi.auth.endpoint.mapper;

import static java.util.UUID.randomUUID;

import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.endpoint.rest.model.User;
import app.esiroi.auth.endpoint.security.Encryptor;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserRestMapper {
  private final PasswordEncoder passwordEncoder;
  private final Encryptor encryptor;

  public User toRest(app.esiroi.auth.model.User user) {
    return new User()
        .id(user.getId())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .otpValidationRequired(user.isOtpValidationRequired())
        .accessToken(user.getAccessToken());
  }

  public app.esiroi.auth.model.User toDomain(AuthUser user) {
    var hashedPass = passwordEncoder.encode(user.getPassword());
    var encryptedSecret = encryptor.getInstance().encrypt("test".getBytes());
    return app.esiroi.auth.model.User.builder()
        .id(randomUUID().toString())
        .email(user.getEmail())
        .passwordHash(hashedPass)
        .otpSecret(encryptedSecret)
        .build();
  }
}
