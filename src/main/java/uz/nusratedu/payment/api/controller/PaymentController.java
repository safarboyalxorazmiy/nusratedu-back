package uz.nusratedu.payment.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.nusratedu.payment.application.dto.*;
import uz.nusratedu.payment.domain.service.IPaymentService;
import uz.nusratedu.user.SecurityUser;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final IPaymentService paymentService;

    @PostMapping("/make")
    public ResponseEntity<PaymentMakeResponseDTO> make(
            @RequestBody PaymentMakeRequestDTO dto,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("/payment/make, Request: {}", dto);

        PaymentMakeResponseDTO response = paymentService.make(dto, user.getTelegramId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/make/gift")
    public ResponseEntity<PaymentMakeResponseDTO> makeGiftPayment(
            @RequestBody PaymentMakeGiftRequestDTO dto,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("/payment/make/gift, Request: {}", dto);

        PaymentMakeResponseDTO response = paymentService.makeGiftPayment(dto, user.getTelegramId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPayment(
            @RequestBody PaymentVerifyRequestDTO dto,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("/payment/verify, Request: {}", dto);

        try {
            paymentService.verify(dto, user.getTelegramId());
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            log.error("Payment verify failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/verify/gift")
    public ResponseEntity<Void> verifyGiftPayment(
            @RequestBody PaymentVerifyGiftRequestDTO dto,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("/payment/verify/gift, Request: {}", dto);

        try {
            paymentService.verifyGiftPayment(dto, user.getTelegramId());
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            log.error("Payment verify failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}