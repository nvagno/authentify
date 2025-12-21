package app.esiroi.auth.integration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import app.esiroi.auth.ITestConfiguration;
import app.esiroi.auth.endpoint.rest.api.HealthApi;
import app.esiroi.auth.endpoint.rest.client.ApiClient;
import app.esiroi.auth.endpoint.rest.client.ApiException;
import org.junit.jupiter.api.Test;

public class HealthControllerIT extends ITestConfiguration {
  @Test
  void health_test_ok() throws ApiException {
    ApiClient apiClient = anApiClient(serverPort);
    HealthApi api = new HealthApi(apiClient);

    var actual = api.ping();

    assertEquals("pong", actual);
  }
}
