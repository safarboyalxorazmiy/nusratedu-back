package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import uz.nusratedu.course.infrastructure.entity.LessonEntity;

import java.util.UUID;

@Repository
public interface LessonRepository extends ReactiveCassandraRepository<LessonEntity, UUID> {
    Flux<LessonEntity> findBySectionId(UUID sectionId);
}