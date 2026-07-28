package app.esiroi.auth.endpoint.security;

import static org.springframework.http.HttpMethod.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConf {
  private final JWTAuthFilter authFilter;

  private final HandlerExceptionResolver handlerExceptionResolver;

  public SecurityConf(
      JWTAuthFilter authFilter,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
    this.authFilter = authFilter;
    this.handlerExceptionResolver = handlerExceptionResolver;
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.exceptionHandling(
            exceptions ->
                exceptions.authenticationEntryPoint(
                    (request, response, authException) ->
                        handlerExceptionResolver.resolveException(
                            request, response, null, authException)))
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/css/**", "/js/**", "/images/**")
                    .permitAll()
                    .requestMatchers(OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(GET, "/ping")
                    .permitAll()
                    .requestMatchers(GET, "/")
                    .permitAll()
                    .requestMatchers(POST, "/login")
                    .permitAll()
                    .requestMatchers(POST, "/logout")
                    .permitAll()
                    .requestMatchers(GET, "/register")
                    .permitAll()
                    .requestMatchers(POST, "/register")
                    .permitAll()
                    .requestMatchers(GET, "/validateOTP")
                    .permitAll()
                    .requestMatchers(POST, "/validateOTP")
                    .permitAll()
                    .requestMatchers(GET, "/qrcode")
                    .permitAll()
                    .requestMatchers(GET, "/profile")
                    .authenticated()
                    .anyRequest()
                    .denyAll())
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
