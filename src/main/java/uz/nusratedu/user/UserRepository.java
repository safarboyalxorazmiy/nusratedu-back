package uz.nusratedu.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CassandraRepository<User, String> {

    @Query("SELECT * FROM users WHERE logincode = ?0 ALLOW FILTERING")
    Optional<User> findByLoginCode(String code);

    @Query("SELECT * FROM users WHERE username = ?0 AND logincode = ?1 ALLOW FILTERING")
    Optional<User> findByUsernameAndLoginCode(String username, String code);

    Optional<User> findByUsername(String username);
    Optional<User> findByTelegramId(String telegramId);
}