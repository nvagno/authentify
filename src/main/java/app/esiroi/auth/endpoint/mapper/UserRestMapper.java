package app.esiroi.auth.endpoint.mapper;

import app.esiroi.auth.endpoint.rest.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserRestMapper {
  public User toRest(app.esiroi.auth.model.User user) {
    return new User()
        .id(user.getId())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .otpValidationRequired(user.isOtpValidationRequired())
        .accessToken(user.getAccessToken());
  }
}
