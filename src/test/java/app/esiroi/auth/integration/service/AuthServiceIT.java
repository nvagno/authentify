package app.esiroi.auth.integration.service;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import app.esiroi.auth.ITestConfiguration;
import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.endpoint.security.TOTPConf;
import app.esiroi.auth.model.User;
import app.esiroi.auth.model.dto.MfaChallenge;
import app.esiroi.auth.model.exception.ForbiddenException;
import app.esiroi.auth.model.exception.NotFoundException;
import app.esiroi.auth.repository.UserRepository;
import app.esiroi.auth.service.AuthService;
import app.esiroi.auth.service.MfaChallengeService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class AuthServiceIT extends ITestConfiguration {
  @Autowired UserRepository userRepository;
  @Autowired AuthService subject;
  @MockBean MfaChallengeService mfaChallengeService;
  @MockBean TOTPConf totpConf;

  @BeforeEach
  void setUp() {
    userRepository.save(expected());
    when(mfaChallengeService.create(anyString())).thenReturn(challenge());
    when(mfaChallengeService.get(anyString())).thenReturn(Optional.of(challenge()));
    when(totpConf.validateTOTP(anyString(), anyString())).thenReturn(true);
  }

  @Test
  void get_user_by_email() {
    var email = "test@email.com";

    var actual = subject.getUserByEmail(email);
    actual.setCreatedAt(null);

    assertEquals(expected(), actual);
  }

  @Test
  void get_user_by_id_ok() {
    var id = expected().getId();

    var actual = subject.getUserById(id);
    actual.setCreatedAt(null);

    assertEquals(expected(), actual);
  }

  @Test
  void get_user_by_id_ko() {
    var id = randomUUID().toString();

    assertThrows(NotFoundException.class, () -> subject.getUserById(id));
  }

  @Test
  void authenticate_user_ok() {
    var toAuthenticate = new AuthUser().email(expected().getEmail()).password("test");

    var actual = subject.authenticateUser(toAuthenticate);

    assertEquals(challenge(), actual);
  }

  @Test
  void authenticate_user_ko() {
    var toAuthenticate = new AuthUser().email(expected().getEmail()).password("wrongpassword");

    assertThrows(ForbiddenException.class, () -> subject.authenticateUser(toAuthenticate));
  }

  @Test
  void validate_otp_ok() {
    String otp = "123456";

    var actual = subject.validateOTP(challenge().getChallengeId(), otp);

    assertNotNull(actual.getAccessToken());
  }

  private User expected() {
    return User.builder()
        .id("user1_id")
        .email("test@email.com")
        .passwordHash("$2a$12$7EAKNtczSkndY4JHkdW9XeLTD3akaoi4Uqmk8wrqfxZF7of8F4kom")
        .otpSecret("*".repeat(16).getBytes())
        .build();
  }

  private MfaChallenge challenge() {
    return new MfaChallenge("challengeId", expected().getId(), 1, null);
  }
}
