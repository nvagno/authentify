package app.esiroi.auth.endpoint.controller;

import app.esiroi.auth.endpoint.rest.model.AuthUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthController {

  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "<h1>pong</h1>";
  }

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("user", new AuthUser());
    return "index";
  }
}
