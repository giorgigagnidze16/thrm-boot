package ge.digiset.prjthrmboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class ThermostatCreatedEvent {
  private Long id;

  private Double temperature;

  public ThermostatCreatedEvent(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "id : " + id;
  }
}
