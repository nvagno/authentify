package app.esiroi.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
  private static final String TOKEN_PREFIX = "AUTH-TOKEN";

  public Cookie putTokenInCookie(String token, HttpServletRequest request) {
    Cookie cookie = new Cookie(TOKEN_PREFIX, token);
    cookie.setSecure(request.isSecure());
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    return cookie;
  }

  public Cookie clearInCookie(HttpServletRequest request) {
    Cookie cookie = new Cookie(TOKEN_PREFIX, null);
    cookie.setSecure(request.isSecure());
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    return cookie;
  }
}
