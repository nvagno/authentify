package app.esiroi.auth.endpoint.controller;

import app.esiroi.auth.endpoint.mapper.UserRestMapper;
import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.endpoint.security.AuthProvider;
import app.esiroi.auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class AuthenticationController {
  private final UserService service;
  private final UserRestMapper mapper;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("user", new AuthUser());
    return "index";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute AuthUser toAuthenticate, HttpServletResponse response) {
    var user = service.authenticateUser(toAuthenticate);
    var cookie = putTokenInCookie(user.getAccessToken());
    response.addCookie(cookie);

    return "redirect:/validateOTP";
  }

  @PostMapping("/logout")
  public String logout(HttpServletResponse response) {
    var cookie = clearInCookie();
    response.addCookie(cookie);
    return "redirect:/";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute AuthUser toRegister) {
    var toSave = mapper.toDomain(toRegister);
    service.saveUser(toSave);
    return "redirect:/validateOTP";
  }

  @GetMapping("/register")
  public String registerPage(Model model) {
    model.addAttribute("user", new AuthUser());
    return "register";
  }

  @GetMapping("/validateOTP")
  public String validateOTP() {
    return "otp";
  }

  @GetMapping("/profile")
  public String profile(Model model) {
    var email = AuthProvider.getAuthenticatedUserEmail();
    model.addAttribute("email", email);
    return "profile";
  }

  @PostMapping("/validateOTP")
  public String validate(@RequestParam("otp") String otp) {
    service.validateOTP(otp);
    return "redirect:/profile";
  }

  private Cookie putTokenInCookie(String token) {
    Cookie cookie = new Cookie("AUTH-TOKEN", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");

    return cookie;
  }

  private Cookie clearInCookie() {
    Cookie cookie = new Cookie("AUTH-TOKEN", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    return cookie;
  }
}
