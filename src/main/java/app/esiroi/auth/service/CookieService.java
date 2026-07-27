package app.esiroi.auth.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

  public Cookie putTokenInCookie(String token) {
    Cookie cookie = new Cookie("AUTH-TOKEN", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");

    return cookie;
  }

  public Cookie clearInCookie() {
    Cookie cookie = new Cookie("AUTH-TOKEN", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    return cookie;
  }
}
