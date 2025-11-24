package uz.nusratedu.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ✅ CONVERTED: From ReactiveCassandraRepository to CassandraRepository
 *
 * All methods return Optional<> instead of Mono<> for blocking operations.
 * These will work seamlessly with virtual threads in servlet mode.
 */
@Repository
public interface UserRepository extends CassandraRepository<User, String> {

    // ✅ CHANGED: Returns Optional<User> instead of Mono<User>
    @Query("SELECT * FROM users WHERE logincode = ?0 ALLOW FILTERING")
    Optional<User> findByLoginCode(String code);

    // ✅ CHANGED: Returns Optional<User> instead of Mono<User>
    @Query("SELECT * FROM users WHERE username = ?0 AND logincode = ?1 ALLOW FILTERING")
    Optional<User> findByUsernameAndLoginCode(String username, String code);

    // Additional useful queries
    Optional<User> findByUsername(String username);
    Optional<User> findByTelegramId(String telegramId);
}