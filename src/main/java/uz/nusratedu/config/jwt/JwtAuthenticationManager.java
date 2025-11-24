package uz.nusratedu.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import uz.nusratedu.user.User;
import uz.nusratedu.user.UserRepository;

import java.util.List;

/**
 * ✅ CONVERTED: From ReactiveAuthenticationManager to AuthenticationProvider
 *
 * This provider authenticates users based on JWT tokens.
 * Works with servlet-based Spring Security (blocking).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authToken = authentication.getCredentials().toString();
        log.debug("Authenticating with JWT token");

        // ✅ CHANGED: Blocking validation instead of Mono<Boolean>
        if (!jwtUtil.validateToken(authToken)) {
            log.warn("Invalid JWT token");
            throw new BadCredentialsException("Invalid JWT token");
        }

        String telegramId = jwtUtil.extractUserId(authToken);
        if (telegramId == null) {
            log.warn("Could not extract user ID from token");
            throw new BadCredentialsException("Invalid token claims");
        }

        // ✅ CHANGED: Blocking repository call instead of Mono<>
        User user = userRepository.findById(telegramId)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", telegramId);
                    return new BadCredentialsException("User not found");
                });

        log.debug("User authenticated: {}", user.getUsername());

        // Build authorities from role
        String role = user.getRole().name();
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}