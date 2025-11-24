package uz.nusratedu.payment.domain.service;

import uz.nusratedu.payment.application.dto.*;

public interface IPaymentService {
    PaymentMakeResponseDTO make(PaymentMakeRequestDTO dto, String telegramId);

    PaymentMakeResponseDTO makeGiftPayment(PaymentMakeGiftRequestDTO dto, String telegramId);

    void verify(PaymentVerifyRequestDTO paymentVerifyRequestDTO, String telegramId);

    void verifyGiftPayment(PaymentVerifyGiftRequestDTO dto, String telegramId);
}