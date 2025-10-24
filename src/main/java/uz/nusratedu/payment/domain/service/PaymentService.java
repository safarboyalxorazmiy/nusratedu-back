package uz.nusratedu.payment.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.payment.application.dto.PaymentMakeRequestDTO;
import uz.nusratedu.payment.application.dto.PaymentMakeResponseDTO;
import uz.nusratedu.payment.application.dto.PaymentVerifyRequestDTO;
import uz.nusratedu.payment.infrastructure.entity.CoursePurchaseHistoryEntity;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;
import uz.nusratedu.payment.infrastructure.repository.CardRepository;
import uz.nusratedu.payment.infrastructure.repository.CoursePurchaseHistoryRepository;
import uz.nusratedu.util.payme.InvalidVerificationCodeException;
import uz.nusratedu.util.payme.PaymeService;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentSevice {
    private final PaymeService paymeService;
    private final CardService cardService;

    private final String authToken = "5e730e8e0b852a417aa49ceb";
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;

    @Override
    public PaymentMakeResponseDTO make(PaymentMakeRequestDTO dto, String telegramId) {
        CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);
        String tokenFromCreateCardRequest =
                paymeService.createCard(dto.getNumber(), dto.getExpire(), card.getOrderId(), authToken);

        String phoneNumberFromVerifyCodeRequest = paymeService.getVerifyCode(tokenFromCreateCardRequest, card.getOrderId(), authToken);
        PaymentMakeResponseDTO response = new PaymentMakeResponseDTO();
        response.setToken(tokenFromCreateCardRequest);
        response.setPhone(phoneNumberFromVerifyCodeRequest);

        cardService.setToken(card.getId(), tokenFromCreateCardRequest);

        return response;
    }

    @Override
    public void verify(PaymentVerifyRequestDTO dto, String telegramId) {
        // TODO MAKE PAYMENT WITH THAT TOKEN
        //....
        CardEntity cardByTokenAndUserId = cardService.getCardByTokenAndUserId(dto.getToken(), telegramId);

        Boolean isCodeAndTokenValid = paymeService.verifyCard(dto.getToken(), dto.getCode(), 123, authToken);
        if (isCodeAndTokenValid.equals(false)) {
            throw new InvalidVerificationCodeException("Code is wrong");
        }

        String productId =
                paymeService.createReceipt(500000, 12333, "Наименование услуги или товара", 250000, 2, "02001001005034001", 12, "1397132");

        paymeService.payReceipt(productId, dto.getToken());

        CoursePurchaseHistoryEntity history = new CoursePurchaseHistoryEntity();
        history.setCourseId(dto.getCourseId());
        history.setCurrency("UZS");
        history.setPaymentMethod("PAYME");
        history.setStatus("ACTIVE");
        history.setPurchasedAt(Instant.now());
        history.setUserId(telegramId);
        history.setPrice(new BigDecimal(250000));
        coursePurchaseHistoryRepository.save(history).subscribe();
    }


}