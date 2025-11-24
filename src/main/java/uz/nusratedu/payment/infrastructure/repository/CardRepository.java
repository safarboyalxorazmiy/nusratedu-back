package uz.nusratedu.payment.infrastructure.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends CassandraRepository<CardEntity, UUID> {

    @AllowFiltering
    List<CardEntity> findByToken(String token);
}