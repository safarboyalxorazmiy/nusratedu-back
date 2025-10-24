package uz.nusratedu.payment.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;
import uz.nusratedu.payment.infrastructure.repository.CardRepository;
import uz.nusratedu.util.payme.CardAccessException;
import uz.nusratedu.util.payme.CardNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CassandraTemplate cassandraTemplate;

    private static final String COUNTER_ID = "card_order_counter";

    private Long getNextOrderId() {
        cassandraTemplate.getCqlOperations()
                .execute("UPDATE order_counter SET value = value + 1 WHERE id = '" + COUNTER_ID + "'");

        Long currentValue = cassandraTemplate
                .selectOne("SELECT value FROM order_counter WHERE id = '" + COUNTER_ID + "'", Long.class);

        if (currentValue == null) {
            cassandraTemplate.getCqlOperations()
                    .execute("INSERT INTO order_counter (id, value) VALUES ('" + COUNTER_ID + "', 1)");
            return 1L;
        }
        return currentValue;
    }

    public CardEntity createCard(String number, String expire, String userId) {
        Long nextOrderId = getNextOrderId();
        CardEntity card = CardEntity.builder()
                .orderId(nextOrderId)
                .number(number)
                .expire(expire)
                .userId(userId)
                .build();
        cardRepository.save(card);
        log.info("Created card orderId={} userId={}", nextOrderId, userId);
        return card;
    }

    public void setToken(UUID cardId, String token) {
        Optional<CardEntity> byId = cardRepository.findById(cardId);
        if (byId.isEmpty()) {
            throw new CardNotFoundException(String.format("Card with id=%s not found", cardId));
        }

        byId.get().setToken(token);
        cardRepository.save(byId.get());
    }

    public Optional<CardEntity> getCardById(UUID id) {
        return cardRepository.findById(id);
    }

    public CardEntity getCardByTokenAndUserId(String token, String userId) {
        Optional<CardEntity> byToken = cardRepository.findByToken(token);
        if (byToken.isEmpty()) {
            throw new CardNotFoundException(String.format("Card with id=%s not found", token));
        }
        if (!byToken.get().getUserId().equals(userId)) {
            throw new CardAccessException("Invalid token");
        }
        return byToken.get();
    }

    public void deleteCard(UUID id) {
        cardRepository.deleteById(id);
    }
}