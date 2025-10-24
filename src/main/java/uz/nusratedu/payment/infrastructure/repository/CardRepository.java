package uz.nusratedu.payment.infrastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends ReactiveCassandraRepository<CardEntity, UUID> {

    Optional<CardEntity> findByToken(String token);
}