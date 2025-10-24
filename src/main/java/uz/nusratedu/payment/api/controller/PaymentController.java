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
import uz.nusratedu.payment.application.dto.PaymentMakeRequestDTO;
import uz.nusratedu.payment.application.dto.PaymentMakeResponseDTO;
import uz.nusratedu.payment.application.dto.PaymentVerifyRequestDTO;
import uz.nusratedu.payment.domain.service.IPaymentSevice;
import uz.nusratedu.user.User;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final IPaymentSevice paymentService;

    @PostMapping("/make")
    public ResponseEntity<PaymentMakeResponseDTO> make(
            @RequestBody PaymentMakeRequestDTO dto
    ) {
        log.info("/payment/make, Request: {}", dto);

        return ResponseEntity.ok(paymentService.make(dto, getUser().getTelegramId()));
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> make(
            @RequestBody PaymentVerifyRequestDTO paymentVerifyRequestDTO
    ) {
        paymentService.verify(paymentVerifyRequestDTO, getUser().getTelegramId());
        return ResponseEntity.status(201).build();
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}