package ge.digiset.prjthrmboot.web.controller;

import ge.digiset.prjthrmboot.dto.ThermostatCreatedEvent;
import ge.digiset.prjthrmboot.model.Thermostat;
import ge.digiset.prjthrmboot.repository.ThermostatRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/noauth")
public class NoAuthController {
  private final ThermostatRepository repository;

  private static boolean isCritical(Thermostat thermostat) {
    if (thermostat.getTemperature() > 0 && thermostat.getThreshold() > 0) {
      return thermostat.getTemperature() >= thermostat.getThreshold();
    } else if (thermostat.getTemperature() < 0 && thermostat.getThreshold() > 0) {
      return false;
    } else if (thermostat.getTemperature() < 0 && thermostat.getThreshold() < 0) {
      return thermostat.getTemperature() <= thermostat.getThreshold();
    }
    return false;
  }

  @PostMapping
  public ResponseEntity<String> updateThermostat(final @RequestBody ThermostatCreatedEvent event) {
    Optional<Thermostat> thermostat = repository.findById(event.getId());
    if (thermostat.isPresent()) {
      Thermostat obj = thermostat.get().ofTemperature(event.getTemperature());
      obj.setCritical(isCritical(obj));
      repository.save(obj);
      return ResponseEntity.ok("Created");
    }
    return ResponseEntity.notFound().build();
  }

}
