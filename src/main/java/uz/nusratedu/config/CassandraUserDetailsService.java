package uz.nusratedu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.nusratedu.user.UserRepository;
import uz.nusratedu.user.SecurityUser;

@Service
@RequiredArgsConstructor
public class CassandraUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String telegramId) {
        return userRepository.findById(telegramId)
                .map(SecurityUser::new);
    }
}