package app.esiroi.auth.endpoint.controller;

import app.esiroi.auth.endpoint.mapper.UserRestMapper;
import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.endpoint.rest.model.User;
import app.esiroi.auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class AuthenticationController {
  private final UserService service;
  private final UserRestMapper mapper;

  @PostMapping("/login")
  public User authenticateUser(@RequestBody AuthUser toAuthenticate) {
    var user = service.authenticateUser(toAuthenticate);
    return mapper.toRest(user);
  }

  @PostMapping("/validateOTP")
  public User validateOTP(@RequestBody String otp) {
    var persisted = service.validateOTP(otp);
    return mapper.toRest(persisted);
  }
}
