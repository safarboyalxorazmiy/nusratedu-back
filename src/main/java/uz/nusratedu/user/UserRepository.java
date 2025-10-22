package uz.nusratedu.user;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCassandraRepository<User, String> {
    @Query("SELECT * FROM users WHERE logincode = ?0 ALLOW FILTERING")
    Mono<User> findByLoginCode(String code);

    @Query("SELECT * FROM users WHERE username = ?0 AND logincode = ?1 ALLOW FILTERING")
    Mono<User> findByUsernameAndLoginCode(String username, String code);
}