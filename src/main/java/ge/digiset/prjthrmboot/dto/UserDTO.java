package ge.digiset.prjthrmboot.dto;

import lombok.extern.jackson.Jacksonized;

@Jacksonized
public record UserDTO(String username,
                      String password) {
}