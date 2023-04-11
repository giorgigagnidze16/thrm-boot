package ge.digiset.prjthrmboot.repository;

import ge.digiset.prjthrmboot.model.Thermostat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ThermostatRepository extends JpaRepository<Thermostat, Long> {
  @Query("SELECT t from Thermostat t WHERE t.user.username = ?1")
  Optional<List<Thermostat>> getUserDevices(String username);
}
