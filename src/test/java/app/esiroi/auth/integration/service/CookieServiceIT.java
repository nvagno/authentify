package app.esiroi.auth.integration.service;

import static org.junit.jupiter.api.Assertions.*;

import app.esiroi.auth.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CookieServiceIT {

  private final CookieService subject = new CookieService();

  @Test
  void put_token_in_cookie() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.isSecure()).thenReturn(true);

    String token = "my-jwt-token";

    Cookie cookie = subject.putTokenInCookie(token, request);

    assertEquals("AUTH-TOKEN", cookie.getName());
    assertEquals(token, cookie.getValue());
    assertTrue(cookie.isHttpOnly());
    assertTrue(cookie.getSecure());
    assertEquals("/", cookie.getPath());
  }

  @Test
  void put_secure_false_when_request_is_not_secure() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.isSecure()).thenReturn(false);

    Cookie cookie = subject.putTokenInCookie("token", request);

    assertFalse(cookie.getSecure());
  }

  @Test
  void clear_cookie() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.isSecure()).thenReturn(true);

    Cookie cookie = subject.clearInCookie(request);

    assertEquals("AUTH-TOKEN", cookie.getName());
    assertNull(cookie.getValue());
    assertTrue(cookie.isHttpOnly());
    assertTrue(cookie.getSecure());
    assertEquals("/", cookie.getPath());
    assertEquals(0, cookie.getMaxAge());
  }
}
