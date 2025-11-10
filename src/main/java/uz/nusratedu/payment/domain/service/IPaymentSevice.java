package uz.nusratedu.payment.domain.service;

import reactor.core.publisher.Mono;
import uz.nusratedu.payment.application.dto.*;

public interface IPaymentSevice {
    Mono<PaymentMakeResponseDTO> make(PaymentMakeRequestDTO dto, String telegramId);
    Mono<PaymentMakeResponseDTO> makeGiftPayment(PaymentMakeGiftRequestDTO dto, String telegramId);


    Mono<Void> verify(PaymentVerifyRequestDTO paymentVerifyRequestDTO, String telegramId);
    Mono<Void> verifyGiftPayment(PaymentVerifyGiftRequestDTO dto, String telegramId);
}
