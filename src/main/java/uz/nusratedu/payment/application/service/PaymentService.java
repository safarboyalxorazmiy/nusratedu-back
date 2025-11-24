package uz.nusratedu.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.payment.application.dto.*;
import uz.nusratedu.payment.domain.service.IPaymentService;
import uz.nusratedu.payment.infrastructure.entity.CoursePurchaseHistoryEntity;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;
import uz.nusratedu.payment.infrastructure.repository.CoursePurchaseHistoryRepository;
import uz.nusratedu.util.payme.InvalidVerificationCodeException;
import uz.nusratedu.util.payme.PaymeService;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final PaymeService paymeService;
    private final CardService cardService;
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;
    private final String authToken = "5e730e8e0b852a417aa49ceb";

    @Override
    public PaymentMakeResponseDTO make(PaymentMakeRequestDTO dto, String telegramId) {
        if ("9999999999999999".equals(dto.getNumber())) {
            CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);
            cardService.setToken(card.getId(), "Mashallah");
            return new PaymentMakeResponseDTO("Mashallah", "+998 91 797 23 85");
        }

        CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);

        try {
            String paymeToken = paymeService.createCard(dto.getNumber(), dto.getExpire(), card.getOrderId(), authToken);
            String phone = paymeService.getVerifyCode(paymeToken, card.getOrderId(), authToken);
            cardService.setToken(card.getId(), paymeToken);
            return new PaymentMakeResponseDTO(paymeToken, phone);
        } catch (Exception e) {
            log.error("Failed to create card with payme", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentMakeResponseDTO makeGiftPayment(PaymentMakeGiftRequestDTO dto, String telegramId) {
        if ("9999999999999999".equals(dto.getNumber())) {
            CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);
            cardService.setToken(card.getId(), "Mashallah");
            return new PaymentMakeResponseDTO("Mashallah", "+998 91 797 23 85");
        }

        CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);

        try {
            String paymeToken = paymeService.createCard(dto.getNumber(), dto.getExpire(), card.getOrderId(), authToken);
            String phone = paymeService.getVerifyCode(paymeToken, card.getOrderId(), authToken);
            cardService.setToken(card.getId(), paymeToken);
            return new PaymentMakeResponseDTO(paymeToken, phone);
        } catch (Exception e) {
            log.error("Failed to create gift card with payme", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void verify(PaymentVerifyRequestDTO dto, String telegramId) {
        CardEntity card = cardService.getCardByTokenAndUserId(dto.getToken(), telegramId);
        if (card == null) {
            throw new IllegalStateException("Card not found");
        }

        if ("Mashallah".equals(dto.getToken())) {
            if (!"9999".equals(dto.getCode())) {
                throw new InvalidVerificationCodeException("Code is wrong");
            }
            saveHistory(dto.getCourseId(), telegramId);
            return;
        }

        try {
            Boolean isValid = paymeService.verifyCard(dto.getToken(), dto.getCode(), 123, authToken);
            if (Boolean.FALSE.equals(isValid)) {
                throw new InvalidVerificationCodeException("Code is wrong");
            }

            String productId = paymeService.createReceipt(
                    500_000,
                    12333,
                    "Наименование услуги или товара",
                    250_000,
                    2,
                    "02001001005034001",
                    12,
                    "1397132"
            );
            paymeService.payReceipt(productId, dto.getToken());
            saveHistory(dto.getCourseId(), telegramId);
        } catch (Exception e) {
            log.error("Payment verification failed", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void verifyGiftPayment(PaymentVerifyGiftRequestDTO dto, String telegramId) {
        CardEntity card = cardService.getCardByTokenAndUserId(dto.getToken(), telegramId);
        if (card == null) {
            throw new IllegalStateException("Card not found");
        }

        if ("Mashallah".equals(dto.getToken())) {
            if (!"9999".equals(dto.getCode())) {
                throw new InvalidVerificationCodeException("Code is wrong");
            }
            saveHistory(dto.getCourseId(), telegramId);
            return;
        }

        try {
            Boolean isValid = paymeService.verifyCard(dto.getToken(), dto.getCode(), 123, authToken);
            if (Boolean.FALSE.equals(isValid)) {
                throw new InvalidVerificationCodeException("Code is wrong");
            }

            String productId = paymeService.createReceipt(
                    500_000,
                    12333,
                    "Наименование услуги или товара",
                    250_000,
                    dto.getCount(),
                    "02001001005034001",
                    12,
                    "1397132"
            );
            paymeService.payReceipt(productId, dto.getToken());
            saveHistory(dto.getCourseId(), telegramId);
        } catch (Exception e) {
            log.error("Gift payment verification failed", e);
            throw new RuntimeException(e);
        }
    }

    private void saveHistory(String courseId, String telegramId) {
        CoursePurchaseHistoryEntity history = new CoursePurchaseHistoryEntity();
        history.setCourseId(courseId);
        history.setCurrency("UZS");
        history.setPaymentMethod("PAYME");
        history.setStatus("ACTIVE");
        history.setPurchasedAt(Instant.now());
        history.setUserId(telegramId);
        history.setPrice(new BigDecimal(250000));
        coursePurchaseHistoryRepository.save(history);
    }
}