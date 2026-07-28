package app.esiroi.auth.unit;

import static app.esiroi.auth.TestData.restUser;
import static app.esiroi.auth.TestData.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import app.esiroi.auth.TestConfigurer;
import app.esiroi.auth.endpoint.mapper.UserRestMapper;
import app.esiroi.auth.endpoint.rest.model.AuthUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRestMapperTest extends TestConfigurer {
  @Autowired private UserRestMapper subject;

  @Test
  void rest_to_domain() {
    var authUser = new AuthUser().email(user().getEmail()).password("test");

    var actual = subject.toDomain(authUser);

    assertEquals(user().getEmail(), actual.getEmail());
    assertNotNull(user().getOtpSecret());
  }

  @Test
  void domain_to_rest() {
    var actual = subject.toRest(user());

    assertEquals(restUser(), actual);
  }
}
