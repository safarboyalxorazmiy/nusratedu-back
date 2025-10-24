package uz.nusratedu.payment.domain.service;

import reactor.core.publisher.Mono;
import uz.nusratedu.payment.application.dto.PaymentMakeRequestDTO;
import uz.nusratedu.payment.application.dto.PaymentMakeResponseDTO;
import uz.nusratedu.payment.application.dto.PaymentVerifyRequestDTO;

public interface IPaymentSevice {
    Mono<PaymentMakeResponseDTO> make(PaymentMakeRequestDTO dto, String telegramId);

    Mono<Void> verify(PaymentVerifyRequestDTO paymentVerifyRequestDTO, String telegramId);
}
