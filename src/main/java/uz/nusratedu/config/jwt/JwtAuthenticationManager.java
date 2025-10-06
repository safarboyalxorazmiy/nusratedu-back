package uz.nusratedu.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import uz.nusratedu.user.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        if (!jwtUtil.validateToken(authToken)) {
            return Mono.empty();
        }

        String telegramId = jwtUtil.extractUserId(authToken);

        return userRepository.findById(telegramId)
                .map(user -> {
                    String role = user.getRole().name(); // 👈 from enum
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    return new UsernamePasswordAuthenticationToken(user, null, authorities);
                });
    }
}