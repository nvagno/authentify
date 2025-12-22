package app.esiroi.auth.repository.configuration;

import app.esiroi.auth.model.dto.MfaChallenge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConf {

  @Bean
  public RedisTemplate<String, MfaChallenge> template(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, MfaChallenge> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);

    Jackson2JsonRedisSerializer<MfaChallenge> serializer =
        new Jackson2JsonRedisSerializer<>(MfaChallenge.class);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);

    return template;
  }
}
