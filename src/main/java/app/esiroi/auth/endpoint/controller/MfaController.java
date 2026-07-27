package app.esiroi.auth.endpoint.controller;

import app.esiroi.auth.endpoint.security.AuthProvider;
import app.esiroi.auth.service.AuthService;
import app.esiroi.auth.service.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@Slf4j
public class MfaController {
  private final AuthService service;
  private final CookieService cookieService;

  @GetMapping("/validateOTP")
  public String validateOTP(@RequestParam("challengeId") String challengeId, Model model) {
    model.addAttribute("challengeId", challengeId);
    return "otp";
  }

  @GetMapping("/qrcode")
  public String qrcode(@RequestParam("email") String email, Model model) {
    var qrCode = service.setupTotp(email);
    model.addAttribute("qrCode", qrCode);
    return "qrcode";
  }

  @GetMapping("/profile")
  public String profile(Model model) {
    var email = AuthProvider.getAuthenticatedUserEmail();
    model.addAttribute("email", email);
    return "profile";
  }

  @PostMapping("/validateOTP")
  public String validate(
      @RequestParam("challengeId") String challengeId,
      @RequestParam("otp") String otp,
      HttpServletResponse response) {
    try {

      var user = service.validateOTP(challengeId, otp);
      var cookie = cookieService.putTokenInCookie(user.getAccessToken());
      response.addCookie(cookie);
      return "redirect:/profile";
    } catch (Exception e) {
      log.error(e.getMessage());
      return "redirect:/validateOTP?challengeId=" + challengeId;
    }
  }
}
