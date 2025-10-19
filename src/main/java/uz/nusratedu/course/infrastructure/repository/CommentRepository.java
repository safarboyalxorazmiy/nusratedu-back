package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import uz.nusratedu.course.infrastructure.entity.CommentEntity;

import java.util.UUID;

public interface CommentRepository extends ReactiveCassandraRepository<CommentEntity, UUID> {
    Flux<CommentEntity> findByLessonId(UUID lessonId);
}