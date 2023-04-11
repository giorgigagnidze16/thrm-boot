package ge.digiset.prjthrmboot.web.controller;

import ge.digiset.prjthrmboot.dto.ThermostatCreatedEvent;
import ge.digiset.prjthrmboot.dto.ThermostatDTO;
import ge.digiset.prjthrmboot.messaging.RabbitMQConfig;
import ge.digiset.prjthrmboot.model.Thermostat;
import ge.digiset.prjthrmboot.model.User;
import ge.digiset.prjthrmboot.repository.ThermostatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/thermostat")
public class ThermostatController {
  private final ThermostatRepository repository;

  private final RabbitTemplate template;

  @PutMapping
  public ResponseEntity<Thermostat> create(
      final @RequestBody ThermostatDTO dto,
      final @RequestHeader("name") String name) {
    try {
      var object = dto.transferDTO().ofUser(name);
      Thermostat updatedThermostat = repository.save(object);
      ThermostatCreatedEvent event = new ThermostatCreatedEvent(updatedThermostat.getId());
      template.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
      return ResponseEntity.ok(updatedThermostat);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostMapping
  public ResponseEntity<Thermostat> update(final @RequestBody Thermostat thermostat,
                                           final @RequestHeader("name") String name) {
    try {
      thermostat.setUser(new User().ofName(name));
      thermostat.setCritical(thermostat.getThreshold() <= thermostat.getTemperature());
      Thermostat updatedThermostat = repository.save(thermostat);
      return ResponseEntity.ok(updatedThermostat);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteById(final @PathVariable Long id) {
    if (repository.existsById(id)) {
      repository.deleteById(id);
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Thermostat> findById(final @PathVariable Long id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @GetMapping("/all")
  public ResponseEntity<List<Thermostat>> getUserDevices(final @RequestHeader("name") String name) {
    return ResponseEntity.of(repository.getUserDevices(name));
  }
}
