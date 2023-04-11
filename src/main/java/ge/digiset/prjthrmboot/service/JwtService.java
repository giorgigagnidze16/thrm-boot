package ge.digiset.prjthrmboot.service;

import ge.digiset.prjthrmboot.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final String KEY = "4226452948404D635166546A576E5A7234753778214125432A462D4A614E6452";

  public String getUsername(String jwt) {
    return getClaim(jwt, Claims::getSubject);
  }

  public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
    return buildToken(claims, userDetails);
  }

  public String generateRefreshToken(
      UserDetails userDetails
  ) {
    return buildToken(new HashMap<>(), userDetails);
  }

  private String buildToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails
  ) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 48))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isValid(String token, UserDetails userDetails) {
    final String username = getUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return getExpiration(token).before(new Date(System.currentTimeMillis()));
  }

  private Date getExpiration(String token) {
    return getClaim(token, Claims::getExpiration);
  }

  public <T> T getClaim(String jwt, Function<Claims, T> resolver) {
    final Claims claims = getClaims(jwt);
    return resolver.apply(claims);
  }

  public UserDTO getUser() {
    return new UserDTO(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), "");
  }

  private Claims getClaims(String jwt) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(jwt)
        .getBody();
  }

  private Key getSignInKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
  }
}
