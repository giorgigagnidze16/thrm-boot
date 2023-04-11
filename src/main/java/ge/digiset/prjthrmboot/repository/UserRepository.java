package ge.digiset.prjthrmboot.repository;

import ge.digiset.prjthrmboot.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
  @Query("SELECT u from User u WHERE u.username = ?1")
  Optional<User> findByUsername(String username);
}
