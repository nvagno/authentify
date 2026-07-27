package app.esiroi.auth.endpoint.controller;

import app.esiroi.auth.endpoint.mapper.UserRestMapper;
import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.service.AuthService;
import app.esiroi.auth.service.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class AuthenticationController {
  private final AuthService service;
  private final UserRestMapper mapper;
  private final CookieService cookieService;

  @PostMapping("/login")
  public String login(@ModelAttribute AuthUser toAuthenticate) {
    try {
      var challengeId = service.authenticateUser(toAuthenticate).getChallengeId();
      return "redirect:/validateOTP?challengeId=" + challengeId;
    } catch (Exception e) {
      return "redirect:/";
    }
  }

  @PostMapping("/logout")
  public String logout(HttpServletResponse response) {
    var cookie = cookieService.clearInCookie();
    response.addCookie(cookie);
    return "redirect:/";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute AuthUser toRegister) {
    var toSave = mapper.toDomain(toRegister);
    var email = service.saveUser(toSave).getEmail();
    return "redirect:/qrcode?email=" + email;
  }

  @GetMapping("/register")
  public String registerPage(Model model) {
    model.addAttribute("user", new AuthUser());
    return "register";
  }
}
