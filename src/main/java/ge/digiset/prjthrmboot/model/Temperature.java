package ge.digiset.prjthrmboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Temperature {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private double temperature;

  @CreationTimestamp
  private Timestamp createdAt;

  @ManyToOne
  private Thermostat thermostat;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Thermostat getThermostat() {
    return thermostat;
  }

  public void setThermostat(Thermostat thermostat) {
    this.thermostat = thermostat;
  }
}
