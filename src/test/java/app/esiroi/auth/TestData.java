package app.esiroi.auth;

import app.esiroi.auth.model.User;
import app.esiroi.auth.model.dto.MfaChallenge;

public class TestData {

  public static User user() {
    return User.builder()
        .id("user1_id")
        .email("test@email.com")
        .passwordHash("$2a$12$7EAKNtczSkndY4JHkdW9XeLTD3akaoi4Uqmk8wrqfxZF7of8F4kom")
        .otpSecret("*".repeat(16).getBytes())
        .build();
  }

  public static MfaChallenge challenge() {
    return new MfaChallenge("challengeId", user().getId(), 1, null);
  }

  public static app.esiroi.auth.endpoint.rest.model.User restUser() {
    return new app.esiroi.auth.endpoint.rest.model.User()
        .id("user1_id")
        .email("test@email.com")
        .accessToken(null)
        .createdAt(null)
        .otpValidationRequired(false);
  }
}
