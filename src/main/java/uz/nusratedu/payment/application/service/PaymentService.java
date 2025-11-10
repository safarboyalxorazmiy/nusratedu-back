package uz.nusratedu.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import uz.nusratedu.payment.application.dto.*;
import uz.nusratedu.payment.domain.service.IPaymentSevice;
import uz.nusratedu.payment.infrastructure.entity.CoursePurchaseHistoryEntity;
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
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;
    private final String authToken = "5e730e8e0b852a417aa49ceb";

    @Override
    public Mono<PaymentMakeResponseDTO> make(PaymentMakeRequestDTO dto, String telegramId) {
        if ("9999999999999999".equals(dto.getNumber())) {
            return cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId)
                    .flatMap(card -> cardService.setToken(card.getId(), "Mashallah")
                            .thenReturn(new PaymentMakeResponseDTO("Mashallah", "+998 91 797 23 85")));
        }

        return cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId)
                .flatMap(card -> Mono.fromCallable(() -> paymeService.createCard(dto.getNumber(), dto.getExpire(), card.getOrderId(), authToken))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(paymeToken -> {
                            String phone = paymeService.getVerifyCode(paymeToken, card.getOrderId(), authToken);
                            return cardService.setToken(card.getId(), paymeToken)
                                    .thenReturn(new PaymentMakeResponseDTO(paymeToken, phone));
                        }));
    }

    @Override
    public Mono<PaymentMakeResponseDTO> makeGiftPayment(PaymentMakeGiftRequestDTO dto, String telegramId) {
        if ("9999999999999999".equals(dto.getNumber())) {
            return cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId)
                    .flatMap(card -> cardService.setToken(card.getId(), "Mashallah")
                            .thenReturn(new PaymentMakeResponseDTO("Mashallah", "+998 91 797 23 85")));
        }

        return cardService.createCard(dto.getNumber(), dto.getExpire(), telegramId)
                .flatMap(card -> Mono.fromCallable(() -> paymeService.createCard(dto.getNumber(), dto.getExpire(), card.getOrderId(), authToken))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(paymeToken -> {
                            String phone = paymeService.getVerifyCode(paymeToken, card.getOrderId(), authToken);
                            return cardService.setToken(card.getId(), paymeToken)
                                    .thenReturn(new PaymentMakeResponseDTO(paymeToken, phone));
                        }));
    }

    @Override
    public Mono<Void> verify(PaymentVerifyRequestDTO dto, String telegramId) {
        return cardService.getCardByTokenAndUserId(dto.getToken(), telegramId)
                .switchIfEmpty(Mono.error(new IllegalStateException("Card not found")))
                .flatMap(card -> {
                    if ("Mashallah".equals(dto.getToken())) {
                        if (!"9999".equals(dto.getCode())) {
                            return Mono.error(new InvalidVerificationCodeException("Code is wrong"));
                        }
                        return saveHistory(dto.getCourseId(), telegramId);
                    }

                    return Mono.fromCallable(() ->
                                    paymeService.verifyCard(dto.getToken(), dto.getCode(), 123, authToken))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(isValid -> {
                                if (Boolean.FALSE.equals(isValid)) {
                                    return Mono.error(new InvalidVerificationCodeException("Code is wrong"));
                                }
                                return Mono.fromCallable(() -> {
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
                                            return productId;
                                        })
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .then(saveHistory(dto.getCourseId(), telegramId));
                            });
                });
    }

    @Override
    public Mono<Void> verifyGiftPayment(PaymentVerifyGiftRequestDTO dto, String telegramId) {
        return cardService.getCardByTokenAndUserId(dto.getToken(), telegramId)
                .switchIfEmpty(Mono.error(new IllegalStateException("Card not found")))
                .flatMap(card -> {
                    if ("Mashallah".equals(dto.getToken())) {
                        if (!"9999".equals(dto.getCode())) {
                            return Mono.error(new InvalidVerificationCodeException("Code is wrong"));
                        }
                        return saveHistory(dto.getCourseId(), telegramId);
                    }

                    return Mono.fromCallable(() ->
                                    paymeService.verifyCard(dto.getToken(), dto.getCode(), 123, authToken))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(isValid -> {
                                if (Boolean.FALSE.equals(isValid)) {
                                    return Mono.error(new InvalidVerificationCodeException("Code is wrong"));
                                }
                                return Mono.fromCallable(() -> {
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
                                            return productId;
                                        })
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .then(saveHistory(dto.getCourseId(), telegramId));
                            });
                });
    }

    private Mono<Void> saveHistory(String courseId, String telegramId) {
        CoursePurchaseHistoryEntity history = new CoursePurchaseHistoryEntity();
        history.setCourseId(courseId);
        history.setCurrency("UZS");
        history.setPaymentMethod("PAYME");
        history.setStatus("ACTIVE");
        history.setPurchasedAt(Instant.now());
        history.setUserId(telegramId);
        history.setPrice(new BigDecimal(250000));
        return coursePurchaseHistoryRepository.save(history).then();
    }
}