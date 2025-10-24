package uz.nusratedu.payment.domain.service;

import uz.nusratedu.payment.application.dto.PaymentMakeRequestDTO;
import uz.nusratedu.payment.application.dto.PaymentMakeResponseDTO;
import uz.nusratedu.payment.application.dto.PaymentVerifyRequestDTO;

public interface IPaymentSevice {
    PaymentMakeResponseDTO make(PaymentMakeRequestDTO dto, String telegramId);

    void verify(PaymentVerifyRequestDTO paymentVerifyRequestDTO, String telegramId);
}
