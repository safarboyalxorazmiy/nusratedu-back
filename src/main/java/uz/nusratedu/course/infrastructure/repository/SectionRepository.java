package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.course.infrastructure.entity.SectionEntity;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: SectionRepository from reactive to blocking
 *
 * Extends CassandraRepository instead of ReactiveCassandraRepository.
 * Returns List<> instead of Flux<>.
 */
@Repository
public interface SectionRepository extends CassandraRepository<SectionEntity, UUID> {

    // ✅ CHANGED: Flux<SectionEntity> → List<SectionEntity>
    List<SectionEntity> findByCourseId(UUID courseId);
}