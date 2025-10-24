package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import uz.nusratedu.course.infrastructure.entity.CompletedLessonEntity;

import java.util.UUID;

public interface CompletedLessonRepository extends ReactiveCassandraRepository<CompletedLessonEntity, UUID> {
    Flux<CompletedLessonEntity> findByUserId(String telegramId);
}