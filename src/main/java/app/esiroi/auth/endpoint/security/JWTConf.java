package app.esiroi.auth.endpoint.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConf {
  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generateToken(String email) {
    long jwtExpiration = 3600000; // 1 hour
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(key)
        .compact();
  }

  public String extractEmail(String token) {
    return getClaims(token).getSubject();
  }

  public boolean validateToken(String token) {
    try {
      getClaims(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}
