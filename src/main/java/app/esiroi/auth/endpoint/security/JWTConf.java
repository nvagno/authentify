package app.esiroi.auth.endpoint.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConf {

  private static final Duration JWT_EXPIRATION = Duration.ofHours(1);

  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generateToken(String email) {
    Instant now = Instant.now();

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(JWT_EXPIRATION)))
        .signWith(key)
        .compact();
  }

  public String extractEmail(String token) {
    return getClaims(token).getSubject();
  }

  public boolean validateToken(String token) {
    if (token == null || token.isBlank()) {
      return false;
    }
    try {
      getClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}
