package uz.nusratedu.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;
import uz.nusratedu.config.jwt.JwtUtil;
import uz.nusratedu.user.UserRepository;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    @PostMapping("/verify")
    public Mono<ResponseEntity<Map<String, String>>> verify(@RequestBody Map<String, String> body) {
        System.out.println("===== /api/v1/auth/verify CALLED =====");
        System.out.println("Incoming body: " + body);

        String code = body.get("code");
        System.out.println("Code received: " + code);

        return userRepo.findByLoginCode(code)
                .doOnSubscribe(sub -> System.out.println("Looking for user with code: " + code))
                .doOnNext(user -> System.out.println("User found: " + user))
                .filter(user -> {
                    boolean valid = !Boolean.TRUE.equals(user.getCodeUsed())
                            && user.getCodeExpiresAt() != null
                            && user.getCodeExpiresAt().isAfter(Instant.now());
                    System.out.println("Validation result for user " + user.getUsername() + ": " + valid);
                    return valid;
                })
                .flatMap(user -> {
                    System.out.println("Marking code as used for user: " + user.getUsername());
                    user.setCodeUsed(true);

                    return userRepo.save(user)
                            .doOnNext(saved -> System.out.println("User saved: " + saved))
                            .map(saved -> {
                                String token = jwtUtil.generateToken(saved.getTelegramId());
                                System.out.println("Generated JWT: " + token);
                                System.out.println("Returning success response for user: " + saved.getUsername());

                                return ResponseEntity.ok(Map.of(
                                        "token", token,
                                        "username", saved.getUsername()
                                ));
                            });
                })
                .doOnError(err -> System.err.println("Error during verification: " + err.getMessage()))
                .switchIfEmpty(Mono.fromRunnable(() ->
                                System.out.println("No valid user found — invalid or expired code"))
                        .then(Mono.just(ResponseEntity.status(401)
                                .body(Map.of("error", "Invalid or expired code"))))
                )
                .doFinally(sig -> System.out.println("===== /verify REQUEST COMPLETED (" + sig + ") ====="));
    }

}