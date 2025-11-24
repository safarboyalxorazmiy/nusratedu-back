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

        log.info("Processing payment request for user: {}, course: {}", user.getTelegramId(), dto.getCourseId());
        log.debug("Payment details - Card ending: {}", dto.getNumber().substring(dto.getNumber().length() - 4));

        PaymentMakeResponseDTO response = paymentService.make(dto, user.getTelegramId());

        log.info("Payment token generated successfully for user: {}", user.getTelegramId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/make/gift")
    public ResponseEntity<PaymentMakeResponseDTO> makeGiftPayment(
            @RequestBody PaymentMakeGiftRequestDTO dto,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Processing gift payment for user: {}, course: {}",
                user.getTelegramId(), dto.getCourseId());
        log.debug("Gift payment details - Card ending: {}", dto.getNumber().substring(dto.getNumber().length() - 4));

        PaymentMakeResponseDTO response = paymentService.makeGiftPayment(dto, user.getTelegramId());

        log.info("Gift payment token generated successfully for user: {}", user.getTelegramId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPayment(
            @RequestBody PaymentVerifyRequestDTO dto,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Verifying payment for user: {}, course: {}", user.getTelegramId(), dto.getCourseId());

        try {
            paymentService.verify(dto, user.getTelegramId());
            log.info("Payment verified successfully for user: {}, course: {}", user.getTelegramId(), dto.getCourseId());
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            log.error("Payment verification failed for user: {}, error: {}", user.getTelegramId(), e.getMessage());
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

        log.info("Verifying gift payment for user: {}, course: {}, count: {}",
                user.getTelegramId(), dto.getCourseId(), dto.getCount());

        try {
            paymentService.verifyGiftPayment(dto, user.getTelegramId());
            log.info("Gift payment verified successfully for user: {}, course: {}",
                    user.getTelegramId(), dto.getCourseId());
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            log.error("Gift payment verification failed for user: {}, error: {}",
                    user.getTelegramId(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}