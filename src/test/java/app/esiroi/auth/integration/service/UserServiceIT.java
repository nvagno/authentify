package app.esiroi.auth.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import app.esiroi.auth.ITestConfiguration;
import app.esiroi.auth.model.User;
import app.esiroi.auth.repository.UserRepository;
import app.esiroi.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceIT extends ITestConfiguration {
  @Autowired UserRepository userRepository;
  @Autowired UserService subject;

  @BeforeEach
  void setUp() {
    userRepository.save(expected());
  }

  @Test
  void get_user_by_email_and_passhash_ok() {
    var email = "test@email.com";

    var actual = subject.getUserByEmail(email);
    actual.setCreatedAt(null);

    assertEquals(expected(), actual);
  }

  private User expected() {
    return User.builder()
        .id("user1_id")
        .email("test@email.com")
        .passwordHash("967520ae23e8ee14888bae72809031b98398ae4a636773e18fff917d77679334")
        .build();
  }
}
