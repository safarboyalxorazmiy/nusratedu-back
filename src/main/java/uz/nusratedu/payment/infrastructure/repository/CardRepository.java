package uz.nusratedu.payment.infrastructure.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends ReactiveCassandraRepository<CardEntity, UUID> {

    @AllowFiltering
    Flux<CardEntity> findByToken(String token);
}