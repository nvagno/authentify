package app.esiroi.auth.integration.controller;

import static app.esiroi.auth.TestData.challenge;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.esiroi.auth.ITestConfiguration;
import app.esiroi.auth.endpoint.mapper.UserRestMapper;
import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.model.User;
import app.esiroi.auth.model.exception.ForbiddenException;
import app.esiroi.auth.service.AuthService;
import app.esiroi.auth.service.CookieService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

class AuthenticationControllerIT extends ITestConfiguration {
  @Autowired private MockMvc mockMvc;

  @MockBean private AuthService service;

  @MockBean private UserRestMapper mapper;

  @MockBean private CookieService cookieService;

  @Test
  void redirect_to_otp() throws Exception {
    when(service.authenticateUser(any(AuthUser.class))).thenReturn(challenge());

    mockMvc
        .perform(
            post("/login")
                .with(csrf())
                .param("email", "test@test.com")
                .param("password", "password"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/validateOTP?challengeId=" + challenge().getChallengeId()));
  }

  @Test
  void redirect_home_when_login_fails() throws Exception {
    when(service.authenticateUser(any(AuthUser.class)))
        .thenThrow(new ForbiddenException("Access denied"));

    mockMvc
        .perform(
            post("/login").param("email", "wrong@test.com").param("password", "bad").with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  void logout_and_clear_cookie() throws Exception {

    Cookie cookie = new Cookie("AUTH-TOKEN", null);

    when(cookieService.clearInCookie(any())).thenReturn(cookie);

    mockMvc.perform(post("/").with(csrf())).andExpect(status().is2xxSuccessful());
  }

  @Test
  void register_user() throws Exception {
    User user = new User();
    user.setEmail("test@test.com");

    when(mapper.toDomain(any(AuthUser.class))).thenReturn(user);

    when(service.saveUser(any(User.class))).thenReturn(user);

    mockMvc
        .perform(
            post("/register")
                .param("email", "test@test.com")
                .param("password", "password")
                .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/qrcode?email=test@test.com"));
  }

  @Test
  void register_page() throws Exception {

    mockMvc
        .perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(model().attributeExists("user"));
  }
}
