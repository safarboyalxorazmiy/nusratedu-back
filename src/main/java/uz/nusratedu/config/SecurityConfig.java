package uz.nusratedu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import uz.nusratedu.config.jwt.JwtAuthenticationManager;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationManager authManager;
    private final ServerSecurityContextRepository contextRepo;

    public SecurityConfig(JwtAuthenticationManager authManager, SecurityContextRepository contextRepo) {
        this.authManager = authManager;
        this.contextRepo = contextRepo;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, CorsConfigurationSource corsConfigSource) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigSource))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/api/v1/course/create").hasAnyRole("ADMIN")
                        .pathMatchers("/api/v1/comment/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/api/v1/lesson/create").hasAnyRole("ADMIN")
                        .pathMatchers("/api/v1/section/create").hasAnyRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .authenticationManager(authManager)
                .securityContextRepository(contextRepo)
                .build();
    }

}