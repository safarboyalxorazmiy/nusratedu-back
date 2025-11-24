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
        log.debug("Initiating payment for user: {}, card: {}", telegramId, dto.getNumber().substring(dto.getNumber().length() - 4));

        if ("9999999999999999".equals(dto.getNumber())) {
            log.info("Test card detected for user: {}", telegramId);
            CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);
            cardService.setToken(card.getId(), "Mashallah");
            return new PaymentMakeResponseDTO("Mashallah", "+998 91 797 23 85");
        }

        CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);
        log.debug("Card entity created with ID: {}", card.getId());

        try {
            String paymeToken = paymeService.createCard(dto.getNumber(), dto.getExpire(), card.getOrderId(), authToken);
            String phone = paymeService.getVerifyCode(paymeToken, card.getOrderId(), authToken);
            cardService.setToken(card.getId(), paymeToken);

            log.info("Payment token created successfully for user: {}", telegramId);
            return new PaymentMakeResponseDTO(paymeToken, phone);
        } catch (Exception e) {
            log.error("Failed to create payment card for user: {}, error: {}", telegramId, e.getMessage(), e);
            throw new RuntimeException("Payment initialization failed", e);
        }
    }

    @Override
    public PaymentMakeResponseDTO makeGiftPayment(PaymentMakeGiftRequestDTO dto, String telegramId) {
        log.debug("Initiating gift payment for user: {}", telegramId);

        if ("9999999999999999".equals(dto.getNumber())) {
            log.info("Test card detected for gift payment, user: {}", telegramId);
            CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);
            cardService.setToken(card.getId(), "Mashallah");
            return new PaymentMakeResponseDTO("Mashallah", "+998 91 797 23 85");
        }

        CardEntity card = cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId);
        log.debug("Gift card entity created with ID: {}", card.getId());

        try {
            String paymeToken = paymeService.createCard(dto.getNumber(), dto.getExpire(), card.getOrderId(), authToken);
            String phone = paymeService.getVerifyCode(paymeToken, card.getOrderId(), authToken);
            cardService.setToken(card.getId(), paymeToken);

            log.info("Gift payment token created successfully for user: {}", telegramId);
            return new PaymentMakeResponseDTO(paymeToken, phone);
        } catch (Exception e) {
            log.error("Failed to create gift payment card for user: {}, error: {}", telegramId, e.getMessage(), e);
            throw new RuntimeException("Gift payment initialization failed", e);
        }
    }

    @Override
    public void verify(PaymentVerifyRequestDTO dto, String telegramId) {
        log.info("Starting payment verification for user: {}, course: {}", telegramId, dto.getCourseId());

        CardEntity card = cardService.getCardByTokenAndUserId(dto.getToken(), telegramId);
        if (card == null) {
            log.error("Card not found for user: {}, token: {}", telegramId, dto.getToken());
            throw new IllegalStateException("Card not found");
        }

        if ("Mashallah".equals(dto.getToken())) {
            log.info("Processing test payment verification for user: {}", telegramId);
            if (!"9999".equals(dto.getCode())) {
                log.warn("Invalid test verification code for user: {}", telegramId);
                throw new InvalidVerificationCodeException("Code is wrong");
            }
            saveHistory(dto.getCourseId(), telegramId);
            log.info("Test payment completed successfully for user: {}", telegramId);
            return;
        }

        try {
            log.debug("Verifying payment with Payme for user: {}", telegramId);
            Boolean isValid = paymeService.verifyCard(dto.getToken(), dto.getCode(), 123, authToken);
            if (Boolean.FALSE.equals(isValid)) {
                log.warn("Invalid verification code for user: {}", telegramId);
                throw new InvalidVerificationCodeException("Code is wrong");
            }

            String productId = paymeService.createReceipt(
                    500_000,
                    12333,
                    "NusratEdu Course Payment",
                    250_000,
                    2,
                    "02001001005034001",
                    12,
                    "1397132"
            );
            paymeService.payReceipt(productId, dto.getToken());
            saveHistory(dto.getCourseId(), telegramId);

            log.info("Payment verified and processed successfully for user: {}, course: {}", telegramId, dto.getCourseId());
        } catch (Exception e) {
            log.error("Payment verification failed for user: {}, error: {}", telegramId, e.getMessage(), e);
            throw new RuntimeException("Payment verification failed", e);
        }
    }

    @Override
    public void verifyGiftPayment(PaymentVerifyGiftRequestDTO dto, String telegramId) {
        log.info("Starting gift payment verification for user: {}, course: {}, count: {}",
                telegramId, dto.getCourseId(), dto.getCount());

        CardEntity card = cardService.getCardByTokenAndUserId(dto.getToken(), telegramId);
        if (card == null) {
            log.error("Card not found for gift payment, user: {}", telegramId);
            throw new IllegalStateException("Card not found");
        }

        if ("Mashallah".equals(dto.getToken())) {
            log.info("Processing test gift payment verification for user: {}", telegramId);
            if (!"9999".equals(dto.getCode())) {
                log.warn("Invalid test verification code for gift payment, user: {}", telegramId);
                throw new InvalidVerificationCodeException("Code is wrong");
            }
            saveHistory(dto.getCourseId(), telegramId);
            log.info("Test gift payment completed successfully for user: {}", telegramId);
            return;
        }

        try {
            log.debug("Verifying gift payment with Payme for user: {}", telegramId);
            Boolean isValid = paymeService.verifyCard(dto.getToken(), dto.getCode(), 123, authToken);
            if (Boolean.FALSE.equals(isValid)) {
                log.warn("Invalid verification code for gift payment, user: {}", telegramId);
                throw new InvalidVerificationCodeException("Code is wrong");
            }

            String productId = paymeService.createReceipt(
                    500_000,
                    12333,
                    "NusratEdu Gift Course Payment",
                    250_000,
                    dto.getCount(),
                    "02001001005034001",
                    12,
                    "1397132"
            );
            paymeService.payReceipt(productId, dto.getToken());
            saveHistory(dto.getCourseId(), telegramId);

            log.info("Gift payment verified and processed successfully for user: {}, course: {}",
                    telegramId, dto.getCourseId());
        } catch (Exception e) {
            log.error("Gift payment verification failed for user: {}, error: {}", telegramId, e.getMessage(), e);
            throw new RuntimeException("Gift payment verification failed", e);
        }
    }

    private void saveHistory(String courseId, String telegramId) {
        log.debug("Saving purchase history for user: {}, course: {}", telegramId, courseId);

        CoursePurchaseHistoryEntity history = new CoursePurchaseHistoryEntity();
        history.setCourseId(courseId);
        history.setCurrency("UZS");
        history.setPaymentMethod("PAYME");
        history.setStatus("ACTIVE");
        history.setPurchasedAt(Instant.now());
        history.setUserId(telegramId);
        history.setPrice(new BigDecimal(250000));

        coursePurchaseHistoryRepository.save(history);
        log.info("Purchase history saved for user: {}, course: {}", telegramId, courseId);
    }
}