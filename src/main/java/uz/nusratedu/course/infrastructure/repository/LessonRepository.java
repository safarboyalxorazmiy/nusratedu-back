package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.course.infrastructure.entity.LessonEntity;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: LessonRepository from reactive to blocking
 *
 * Extends CassandraRepository instead of ReactiveCassandraRepository.
 * Returns List<> instead of Flux<>.
 */
@Repository
public interface LessonRepository extends CassandraRepository<LessonEntity, UUID> {

    // ✅ CHANGED: Flux<LessonEntity> → List<LessonEntity>
    List<LessonEntity> findBySectionId(UUID sectionId);
}