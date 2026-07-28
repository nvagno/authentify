package app.esiroi.auth.integration.service;

import static app.esiroi.auth.TestData.challenge;
import static app.esiroi.auth.TestData.user;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import app.esiroi.auth.TestConfigurer;
import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.endpoint.security.TOTPConf;
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

class AuthServiceIT extends TestConfigurer {
  @Autowired UserRepository userRepository;
  @Autowired AuthService subject;
  @MockBean MfaChallengeService mfaChallengeService;
  @MockBean TOTPConf totpConf;

  @BeforeEach
  void setUp() {
    userRepository.save(user());
    when(mfaChallengeService.create(anyString())).thenReturn(challenge());
    when(mfaChallengeService.get(anyString())).thenReturn(Optional.of(challenge()));
    when(totpConf.validateTOTP(anyString(), anyString())).thenReturn(true);
  }

  @Test
  void get_user_by_email() {
    var email = "test@email.com";

    var actual = subject.getUserByEmail(email);
    actual.setCreatedAt(null);

    assertEquals(user(), actual);
  }

  @Test
  void get_user_by_id_ok() {
    var id = user().getId();

    var actual = subject.getUserById(id);
    actual.setCreatedAt(null);

    assertEquals(user(), actual);
  }

  @Test
  void get_user_by_id_ko() {
    var id = randomUUID().toString();

    assertThrows(NotFoundException.class, () -> subject.getUserById(id));
  }

  @Test
  void authenticate_user_ok() {
    var toAuthenticate = new AuthUser().email(user().getEmail()).password("test");

    var actual = subject.authenticateUser(toAuthenticate);

    assertEquals(challenge(), actual);
  }

  @Test
  void authenticate_user_ko() {
    var toAuthenticate = new AuthUser().email(user().getEmail()).password("wrongpassword");

    assertThrows(ForbiddenException.class, () -> subject.authenticateUser(toAuthenticate));
  }

  @Test
  void validate_otp_ok() {
    String otp = "123456";

    var actual = subject.validateOTP(challenge().getChallengeId(), otp);

    assertNotNull(actual.getAccessToken());
  }
}
