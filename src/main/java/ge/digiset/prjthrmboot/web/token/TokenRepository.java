package ge.digiset.prjthrmboot.web.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Integer> {
  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.username = u.username\s
      where u.username = :username and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(@Param("username") String username);

  Optional<Token> findByToken(String token);
}