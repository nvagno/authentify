package app.esiroi.auth.integration.controller;

import static app.esiroi.auth.TestData.challenge;
import static app.esiroi.auth.TestData.user;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.esiroi.auth.TestConfigurer;
import app.esiroi.auth.model.User;
import app.esiroi.auth.service.AuthService;
import app.esiroi.auth.service.CookieService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

class MfaControllerIT extends TestConfigurer {
  @Autowired private MockMvc mockMvc;

  @MockBean private AuthService service;

  @MockBean private CookieService cookieService;

  @Test
  void validate_otp_page() throws Exception {
    var challengeId = challenge().getChallengeId();

    mockMvc
        .perform(get("/validateOTP").param("challengeId", challengeId).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("otp"))
        .andExpect(model().attribute("challengeId", challengeId));
  }

  @Test
  void display_qrcode_page() throws Exception {
    var userEmail = user().getEmail();
    var qrCodeValue = "qr-code-value";

    when(service.setupTotp(userEmail)).thenReturn(qrCodeValue);

    mockMvc
        .perform(get("/qrcode").param("email", userEmail).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("qrcode"))
        .andExpect(model().attribute("qrCode", qrCodeValue));
  }

  @Test
  void redirect_to_profile_when_otp_is_valid() throws Exception {
    var jwtToken = "jwt-token";
    var challengeId = challenge().getChallengeId();
    var totp = "123456";

    var user = mock(User.class);
    when(user.getAccessToken()).thenReturn(jwtToken);

    when(service.validateOTP(challengeId, totp)).thenReturn(user);

    Cookie cookie = new Cookie("AUTH-TOKEN", jwtToken);

    when(cookieService.putTokenInCookie(eq(jwtToken), any())).thenReturn(cookie);

    mockMvc
        .perform(
            post("/validateOTP").param("challengeId", challengeId).param("otp", totp).with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/profile"));
  }

  @Test
  void redirect_to_validate_otp_when_validation_fails() throws Exception {
    var challengeId = challenge().getChallengeId();
    var totp = "123456";

    when(service.validateOTP(challengeId, totp)).thenThrow(new RuntimeException());

    mockMvc
        .perform(
            post("/validateOTP").param("challengeId", challengeId).param("otp", totp).with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/validateOTP?challengeId=" + challengeId));
  }
}
