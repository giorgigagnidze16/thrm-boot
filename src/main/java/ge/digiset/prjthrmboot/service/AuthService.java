package ge.digiset.prjthrmboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.digiset.prjthrmboot.dto.UserDTO;
import ge.digiset.prjthrmboot.model.User;
import ge.digiset.prjthrmboot.repository.UserRepository;
import ge.digiset.prjthrmboot.web.controller.auth.AuthResponse;
import ge.digiset.prjthrmboot.web.token.Token;
import ge.digiset.prjthrmboot.web.token.TokenRepository;
import ge.digiset.prjthrmboot.web.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository repository;

  private final PasswordEncoder encoder;

  private final JwtService jwtService;

  private final AuthenticationProvider manager;

  private final TokenRepository tokenRepository;

  public ResponseEntity<AuthResponse> register(UserDTO user) {
    if (userExists(user.username())) {
      return ResponseEntity.badRequest().build();
    }
    User u = User.builder()
        .username(user.username())
        .password(encoder.encode(user.password()))
        .build();
    u = repository.save(u);
    var token = jwtService.generateToken(new HashMap<>(), u);
    var refreshToken = jwtService.generateRefreshToken(u);
    saveUserToken(u, token);
    return ResponseEntity.ok(
        AuthResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .build()
    );
  }

  public Optional<AuthResponse> authenticate(UserDTO user) {
    try {
      manager.authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
    } catch (AuthenticationException e) {
      return Optional.empty();
    }
    var u =
        repository.findByUsername(user.username()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    var token = jwtService.generateToken(new HashMap<>(), u);
    var refreshToken = jwtService.generateRefreshToken(u);
    revokeAllUserTokens(u);
    saveUserToken(u, token);
    return Optional.of(AuthResponse.builder().token(token).refreshToken(refreshToken).build());
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUsername());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String username;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    username = jwtService.getUsername(refreshToken);
    if (username != null) {
      var user = this.repository.findByUsername(username)
          .orElseThrow();
      if (jwtService.isValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(new HashMap<>(), user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthResponse.builder()
            .token(accessToken)
            .refreshToken(refreshToken)
            .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  private boolean userExists(String username) {
    return repository.findByUsername(username).isPresent();
  }
}
