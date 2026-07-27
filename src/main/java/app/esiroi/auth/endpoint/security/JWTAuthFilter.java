package app.esiroi.auth.endpoint.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
  private final JWTConf jwtConf;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var cookies = request.getCookies();
    if (cookies != null) {
      String token =
          Arrays.stream(request.getCookies())
              .filter(cookie -> "AUTH-TOKEN".equals(cookie.getName()))
              .map(Cookie::getValue)
              .findFirst()
              .orElse(null);

      if (jwtConf.validateToken(token)) {
        String email = jwtConf.extractEmail(token);

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    filterChain.doFilter(request, response);
  }
}
