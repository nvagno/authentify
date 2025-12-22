package app.esiroi.auth.service;

import static java.util.UUID.randomUUID;

import app.esiroi.auth.model.dto.MfaChallenge;
import java.time.Duration;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MfaChallengeService {
  private static final Duration TTL = Duration.ofMinutes(2);
  private final RedisTemplate<String, MfaChallenge> redisTemplate;

  private String key(String id) {
    return "mfa:challenge:" + id;
  }

  public MfaChallenge create(String userId) {
    MfaChallenge c = new MfaChallenge();
    c.setChallengeId(randomUUID().toString());
    c.setUserId(userId);
    c.setAttempts(0);
    // c.setCreatedAt(now()); Not supported for now

    redisTemplate.opsForValue().set(key(c.getChallengeId()), c, TTL);

    return c;
  }

  public Optional<MfaChallenge> get(String id) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(key(id)));
  }

  public void delete(String id) {
    redisTemplate.delete(key(id));
  }
}
