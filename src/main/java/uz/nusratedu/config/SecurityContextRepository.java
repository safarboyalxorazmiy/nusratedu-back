package uz.nusratedu.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.nusratedu.config.jwt.JwtUtil;
import uz.nusratedu.user.SecurityUser;
import uz.nusratedu.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ✅ CONVERTED: From ReactiveSecurityContextRepository to OncePerRequestFilter
 *
 * This filter extracts JWT from Authorization header and sets up SecurityContext.
 * Replaces the reactive repository pattern in servlet world.
 *
 * Flow:
 * 1. Extract Bearer token from Authorization header
 * 2. Validate token signature and expiration
 * 3. Load user from database
 * 4. Set Security Context (thread-local)
 * 5. Continue request
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityContextRepository extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String userId = jwtUtil.extractUserId(jwt);

                if (userId != null) {
                    // ✅ CHANGED: Blocking repository call instead of Mono<>
                    userRepository.findById(userId).ifPresent(user -> {
                        SecurityUser securityUser = new SecurityUser(user);
                        var authentication = new UsernamePasswordAuthenticationToken(
                                securityUser,
                                null,
                                securityUser.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
                }
            }
        } catch (Exception ex) {
            log.error("Error setting user authentication: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}