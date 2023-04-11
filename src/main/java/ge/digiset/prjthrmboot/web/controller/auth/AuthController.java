package ge.digiset.prjthrmboot.web.controller.auth;


import ge.digiset.prjthrmboot.dto.UserDTO;
import ge.digiset.prjthrmboot.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService service;

  @GetMapping
  public String getHello() {
    return "Hello";
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(final @RequestBody UserDTO user) {
    var authResponse = service.authenticate(user);
    return authResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @PostMapping("/refresh-tokens")
  public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
    service.refreshToken(req, res);
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(final @RequestBody UserDTO user) {
    return service.register(user);
  }
}
