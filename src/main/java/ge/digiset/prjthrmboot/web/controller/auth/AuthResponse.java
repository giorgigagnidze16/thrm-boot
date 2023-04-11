package ge.digiset.prjthrmboot.web.controller.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
  @JsonProperty("accessToken")
  private String token;

  @JsonProperty("refreshToken")
  private String refreshToken;
}
