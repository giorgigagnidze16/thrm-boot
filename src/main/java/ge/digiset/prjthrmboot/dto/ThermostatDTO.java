package ge.digiset.prjthrmboot.dto;

import ge.digiset.prjthrmboot.model.Location;
import ge.digiset.prjthrmboot.model.Thermostat;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
public record ThermostatDTO(String name,
                            Location location,
                            double threshold
) {
  public Thermostat transferDTO() {
    Thermostat thermostat = new Thermostat();
    thermostat.setName(name);
    thermostat.setLocation(location);
    thermostat.setThreshold(threshold);
    return thermostat;
  }
}
