package app.esiroi.auth;

import app.esiroi.auth.model.User;
import app.esiroi.auth.model.dto.MfaChallenge;

public class TestData {

  public static User expected() {
    return User.builder()
        .id("user1_id")
        .email("test@email.com")
        .passwordHash("$2a$12$7EAKNtczSkndY4JHkdW9XeLTD3akaoi4Uqmk8wrqfxZF7of8F4kom")
        .otpSecret("*".repeat(16).getBytes())
        .build();
  }

  public static MfaChallenge challenge() {
    return new MfaChallenge("challengeId", expected().getId(), 1, null);
  }
}
