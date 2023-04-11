package ge.digiset.prjthrmboot.web.config;

import ge.digiset.prjthrmboot.repository.UserRepository;
import ge.digiset.prjthrmboot.service.JwtService;
import java.io.IOException;
import java.util.Objects;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String token;
    final String username;
    if (Objects.isNull(header) || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    token = header.substring(7);
    username = jwtService.getUsername(token);

    if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
      UserDetails u = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
      if (jwtService.isValid(token, u)) {
        log.info("Token is valid, {}", u.getUsername());
        var authToken = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      } else {
        log.error("Cant validate token, {}", u);
      }
    }
    filterChain.doFilter(request, response);
  }

}
