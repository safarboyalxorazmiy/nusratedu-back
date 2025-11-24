package uz.nusratedu.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.nusratedu.config.jwt.JwtUtil;
import uz.nusratedu.user.User;
import uz.nusratedu.user.UserRepository;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    // ✅ CONVERTED: No more Mono<>, just blocking calls
    // Virtual threads handle the blocking efficiently
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestBody Map<String, String> body) {
        log.info("===== /api/v1/auth/verify CALLED =====");
        log.debug("Incoming body: {}", body);

        String code = body.get("code");
        log.debug("Code received: {}", code);

        try {
            // ✅ CHANGED: Blocking call instead of Mono
            User user = userRepo.findByLoginCode(code)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or expired code"));

            log.debug("User found: {}", user.getUsername());

            // Validate code
            boolean valid = !Boolean.TRUE.equals(user.getCodeUsed())
                    && user.getCodeExpiresAt() != null
                    && user.getCodeExpiresAt().isAfter(Instant.now());

            if (!valid) {
                log.warn("Validation failed for user: {}", user.getUsername());
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Invalid or expired code"));
            }

            log.debug("Marking code as used for user: {}", user.getUsername());
            user.setCodeUsed(true);
            user = userRepo.save(user);

            log.debug("User saved: {}", user.getUsername());
            String token = jwtUtil.generateToken(user.getTelegramId());
            log.debug("Generated JWT token for user: {}", user.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", user.getUsername(),
                    "role", user.getRole().toString()
            ));

        } catch (IllegalArgumentException ex) {
            log.warn("No valid user found: {}", ex.getMessage());
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid or expired code"));
        } catch (Exception ex) {
            log.error("Error during verification: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Internal server error"));
        } finally {
            log.info("===== /verify REQUEST COMPLETED =====");
        }
    }

    @PostMapping("/admin/verify")
    public ResponseEntity<Map<String, String>> adminVerify(@RequestBody Map<String, String> body) {
        log.info("===== /api/v1/auth/admin/verify CALLED =====");
        log.debug("Incoming body: {}", body);

        String username = body.get("username");
        String code = body.get("code");
        log.debug("Admin verification for username: {}, code: {}", username, code);

        try {
            // ✅ CHANGED: Blocking call instead of Mono
            User user = userRepo.findByUsernameAndLoginCode(username, code)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or expired code"));

            log.debug("User found: {}", user.getUsername());

            // Validate code
            boolean valid = !Boolean.TRUE.equals(user.getCodeUsed())
                    && user.getCodeExpiresAt() != null
                    && user.getCodeExpiresAt().isAfter(Instant.now());

            if (!valid) {
                log.warn("Validation failed for user: {}", user.getUsername());
                return ResponseEntity.status(401)
                        .body(Map.of("error", "Invalid or expired code"));
            }

            // ✅ CHANGED: Simple if-else instead of reactive conditionals
            if (user.getRole() == null || !"ADMIN".equalsIgnoreCase(user.getRole().toString())) {
                log.warn("Unauthorized role for verification: {}", user.getRole());
                return ResponseEntity.status(403)
                        .body(Map.of("error", "Access denied: only admins can verify."));
            }

            log.debug("Marking code as used for admin user: {}", user.getUsername());
            user.setCodeUsed(true);
            user = userRepo.save(user);

            log.debug("Admin user saved: {}", user.getUsername());
            String token = jwtUtil.generateToken(user.getTelegramId());
            log.debug("Generated JWT token for admin user: {}", user.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", user.getUsername(),
                    "role", user.getRole().toString()
            ));

        } catch (IllegalArgumentException ex) {
            log.warn("No valid admin user found: {}", ex.getMessage());
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid or expired code"));
        } catch (Exception ex) {
            log.error("Error during admin verification: {}", ex.getMessage(), ex);
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Internal server error"));
        } finally {
            log.info("===== /admin/verify REQUEST COMPLETED =====");
        }
    }
}