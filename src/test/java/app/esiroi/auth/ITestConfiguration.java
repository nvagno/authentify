package app.esiroi.auth;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import app.esiroi.auth.endpoint.rest.client.ApiClient;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class ITestConfiguration {
  @LocalServerPort protected int serverPort;

  public static ApiClient anApiClient(int serverPort) {
    var client = new ApiClient();
    client.setPort(serverPort);
    client.setScheme("http");
    client.setHost("localhost");
    return client;
  }

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("crypto.secret.key", () -> "test");
    registry.add("crypto.salt", () -> "a1b2c3d4e5f67890abcdef1234567890");
    registry.add("spring.flyway.enabled", () -> true);
    registry.add("spring.flyway.locations", () -> "classpath:db/migration");
  }

  @TestConfiguration
  static class TestConfig {
    @Bean
    public DataSource dataSource() {
      return DataSourceBuilder.create()
          .driverClassName("org.h2.Driver")
          .url(
              "jdbc:h2:mem:testdb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH")
          .username("sa")
          .password("")
          .build();
    }
  }
}
