package ge.digiset.prjthrmboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;


@Entity
@Builder
@ToString
@Jacksonized
@AllArgsConstructor
public class Thermostat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Location location;

  private double temperature;

  private double threshold;

  private boolean isCritical;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "username", referencedColumnName = "username")
  private User user;

  public Thermostat(Long id, String name, Location location, double threshold) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.threshold = threshold;
  }

  public Thermostat() {
    this(null, null, null, 0);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public double getThreshold() {
    return threshold;
  }

  public void setThreshold(double threshold) {
    this.threshold = threshold;
  }

  public boolean isCritical() {
    return isCritical;
  }

  public void setCritical(boolean critical) {
    isCritical = critical;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public Thermostat ofUser(String username) {
    setUser(User.builder().username(username).build());
    return this;
  }

  public Thermostat ofTemperature(Double temperature) {
    setTemperature(temperature);
    return this;
  }
}
