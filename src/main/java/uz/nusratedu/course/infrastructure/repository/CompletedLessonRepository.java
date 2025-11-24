package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.course.infrastructure.entity.CompletedLessonEntity;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: CompletedLessonRepository from reactive to blocking
 *
 * Extends CassandraRepository instead of ReactiveCassandraRepository.
 * Returns List<> instead of Flux<>.
 */
@Repository
public interface CompletedLessonRepository extends CassandraRepository<CompletedLessonEntity, UUID> {

    // ✅ CHANGED: Flux<CompletedLessonEntity> → List<CompletedLessonEntity>
    List<CompletedLessonEntity> findByUserId(String telegramId);
}