package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.course.infrastructure.entity.CommentEntity;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: CommentRepository from reactive to blocking
 *
 * Extends CassandraRepository instead of ReactiveCassandraRepository.
 * Returns List<> instead of Flux<>.
 */
@Repository
public interface CommentRepository extends CassandraRepository<CommentEntity, UUID> {

    // ✅ CHANGED: Flux<CommentEntity> → List<CommentEntity>
    List<CommentEntity> findByLessonId(UUID lessonId);
}