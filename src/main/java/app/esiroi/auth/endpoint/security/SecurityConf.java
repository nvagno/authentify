package app.esiroi.auth.endpoint.security;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.web.cors.CorsConfiguration.ALL;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Slf4j
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConf {
  private final JWTAuthFilter authFilter;

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.cors(corsConf -> corsConf.configurationSource(corsConfigurationSource()))
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(GET, "/v1/ping")
                    .permitAll()
                    .requestMatchers(POST, "/v1/login")
                    .permitAll()
                    .requestMatchers(POST, "/v1/validateOTP")
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(ALL));
    config.setAllowedMethods(List.of(ALL));
    config.setAllowedHeaders(List.of(ALL));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}

