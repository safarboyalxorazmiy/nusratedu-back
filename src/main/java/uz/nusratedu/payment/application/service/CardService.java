package uz.nusratedu.payment.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.CqlOperations;
import org.springframework.stereotype.Service;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;
import uz.nusratedu.payment.infrastructure.repository.CardRepository;
import uz.nusratedu.util.payme.CardNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CassandraTemplate cassandraTemplate;

    private static final String COUNTER_ID = "card_order_counter";

    private CqlOperations cql() {
        return cassandraTemplate.getCqlOperations();
    }

    private Long getNextOrderId() {
        try {
            cql().execute("UPDATE order_counter SET value = value + 1 WHERE id = ?", COUNTER_ID);

            List<Long> result = cql().query(
                    "SELECT value FROM order_counter WHERE id = ?",
                    (row, rowNum) -> row.getLong("value"),
                    COUNTER_ID
            );

            if (result != null && !result.isEmpty()) {
                Long value = result.get(0);
                if (value != null) {
                    log.debug("Next orderId: {}", value);
                    return value;
                }
            }
        } catch (Exception e) {
            log.error("Failed to get next orderId", e);
        }

        return initializeCounterFallback();
    }

    private Long initializeCounterFallback() {
        try {
            cql().execute(
                    "INSERT INTO order_counter (id, value) VALUES (?, 1) IF NOT EXISTS",
                    COUNTER_ID
            );
            log.info("Initialized order_counter with value=1");
            return 1L;
        } catch (Exception ex) {
            log.error("Critical: Failed to initialize counter", ex);
            return System.currentTimeMillis() % 100_000;
        }
    }

    public CardEntity createCard(String number, String expire, String userId) {
        Long orderId = getNextOrderId();

        CardEntity card = CardEntity.builder()
                .orderId(orderId)
                .number(number)
                .expire(expire)
                .userId(userId)
                .build();

        CardEntity saved = cardRepository.save(card);
        log.info("Card created: orderId={} cardId={} userId={}", orderId, saved.getId(), userId);
        return saved;
    }

    public void setToken(UUID cardId, String token) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardId));

        card.setToken(token);
        cardRepository.save(card);
    }

    public CardEntity getCardById(UUID id) {
        return cardRepository.findById(id).orElse(null);
    }

    public CardEntity getCardByTokenAndUserId(String token, String userId) {
        List<CardEntity> cards = cardRepository.findByToken(token);

        return cards.stream()
                .filter(card -> userId.equals(card.getUserId()))
                .max((card1, card2) -> {
                    LocalDateTime date1 = card1.getCreatedAt() != null ? card1.getCreatedAt() : LocalDateTime.MIN;
                    LocalDateTime date2 = card2.getCreatedAt() != null ? card2.getCreatedAt() : LocalDateTime.MIN;
                    return date2.compareTo(date1);
                })
                .orElse(null);
    }
}