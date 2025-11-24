package uz.nusratedu.test.infastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import uz.nusratedu.payment.infrastructure.entity.card.CardEntity;

import java.util.UUID;

public interface TestQuestionRepository extends CassandraRepository<CardEntity, UUID> {
}
