package uz.nusratedu.payment.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import uz.nusratedu.payment.application.dto.PaymentMakeRequestDTO;
import uz.nusratedu.payment.application.dto.PaymentMakeResponseDTO;
import uz.nusratedu.payment.application.dto.PaymentVerifyRequestDTO;
import uz.nusratedu.payment.domain.service.IPaymentSevice;
import uz.nusratedu.user.User;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final IPaymentSevice paymentService;

    @PostMapping("/make")
    public ResponseEntity<Mono<PaymentMakeResponseDTO>> make(
            @RequestBody PaymentMakeRequestDTO dto,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        log.info("/payment/make, Request: {}", dto);

        return ResponseEntity.ok(paymentService.make(dto, user.getTelegramId()));
    }

    @PostMapping("/verify")
    public Mono<ResponseEntity<Void>> make(
            @RequestBody PaymentVerifyRequestDTO dto,
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        log.info("/payment/verify, Request: {}", dto);

        return paymentService.verify(dto, user.getTelegramId())
                .then(Mono.just(ResponseEntity.status(201).<Void>build()))
                .onErrorResume(e -> {
                    log.error("Payment verify failed: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

}