package uz.nusratedu.payment.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.cql.ReactiveCqlOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;
import uz.nusratedu.payment.infrastructure.repository.CardRepository;
import uz.nusratedu.util.payme.CardNotFoundException;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ReactiveCassandraTemplate cassandraTemplate;

    private static final String COUNTER_ID = "card_order_counter";

    private ReactiveCqlOperations cql() {
        return cassandraTemplate.getReactiveCqlOperations();
    }

    private Mono<Long> getNextOrderId() {
        return cql().execute("UPDATE order_counter SET value = value + 1 WHERE id = ?", COUNTER_ID)
                .then(
                        cql().queryForObject(
                                "SELECT value FROM order_counter WHERE id = ?",
                                Long.class,
                                COUNTER_ID
                        )
                )
                .flatMap(value -> {
                    if (value == null) {
                        log.warn("Counter value is null, falling back to initialization");
                        return initializeCounterFallback();
                    }
                    log.debug("Next orderId: {}", value);
                    return Mono.just(value);
                })
                .onErrorResume(e -> {
                    log.error("Failed to get next orderId", e);
                    return initializeCounterFallback();
                });
    }

    private Mono<Long> initializeCounterFallback() {
        return cql().execute(
                        "INSERT INTO order_counter (id, value) VALUES (?, 1) IF NOT EXISTS",
                        COUNTER_ID
                )
                .thenReturn(1L)
                .doOnSuccess(v -> log.info("Initialized order_counter with value=1"))
                .onErrorResume(ex -> {
                    log.error("Critical: Failed to initialize counter", ex);
                    return Mono.just(System.currentTimeMillis() % 100_000); // emergency fallback
                });
    }

    public Mono<CardEntity> createCard(String number, String expire, String userId) {
        return getNextOrderId()
                .flatMap(orderId -> {
                    CardEntity card = CardEntity.builder()
                            .orderId(orderId)
                            .number(number)
                            .expire(expire)
                            .userId(userId)
                            .build();

                    return cardRepository.save(card)
                            .doOnSuccess(saved -> log.info("Card created: orderId={} cardId={} userId={}",
                                    orderId, saved.getId(), userId));
                });
    }

    public Mono<Void> setToken(UUID cardId, String token) {
        return cardRepository.findById(cardId)
                .switchIfEmpty(Mono.error(new CardNotFoundException("Card not found: " + cardId)))
                .flatMap(card -> {
                    card.setToken(token);
                    return cardRepository.save(card);
                })
                .then();
    }

    public Mono<CardEntity> getCardById(UUID id) {
        return cardRepository.findById(id);
    }

    public Mono<CardEntity> getCardByTokenAndUserId(String token, String userId) {
        return Mono.fromCallable(() -> cardRepository.findByToken(token))
                .flatMap(optionalCard -> optionalCard
                        .filter(card -> card.getUserId().equals(userId))
                        .map(Mono::just)
                        .orElseGet(() -> Mono.error(new CardNotFoundException("Invalid token")))
                );
    }
}
