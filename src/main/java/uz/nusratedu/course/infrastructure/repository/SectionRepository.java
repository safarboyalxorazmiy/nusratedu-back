package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import uz.nusratedu.course.infrastructure.entity.SectionEntity;

import java.util.UUID;

@Repository
public interface SectionRepository extends ReactiveCassandraRepository<SectionEntity, UUID> {
    Flux<SectionEntity> findByCourseId(UUID courseId);
}