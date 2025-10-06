package uz.nusratedu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import uz.nusratedu.config.jwt.JwtAuthenticationManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationManager authManager;
    private final SecurityContextRepository contextRepo;

    public SecurityConfig(JwtAuthenticationManager authManager, SecurityContextRepository contextRepo) {
        this.authManager = authManager;
        this.contextRepo = contextRepo;
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/v1/admin/**").hasRole("ADMIN") // 👈 only admin users
                        .pathMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN") // 👈 normal users
                        .anyExchange().authenticated()
                )
                .authenticationManager(authManager)
                .securityContextRepository(contextRepo)
                .build();
    }

}