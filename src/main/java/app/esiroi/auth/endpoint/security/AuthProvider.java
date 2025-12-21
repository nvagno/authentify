package app.esiroi.auth.endpoint.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider {
  public static String getAuthenticatedUserEmail() {
    return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
