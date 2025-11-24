package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;

import java.util.UUID;

/**
 * ✅ CONVERTED: CourseRepository from reactive to blocking
 *
 * Extends CassandraRepository instead of ReactiveCassandraRepository.
 * Inherits findAll() and other standard CRUD methods from CassandraRepository.
 * All return blocking types (Iterable<>, Optional<>, etc.)
 */
@Repository
public interface CourseRepository extends CassandraRepository<CourseEntity, UUID> {
    // findAll() inherited from CassandraRepository
    // findById(UUID) inherited from CassandraRepository
    // save() inherited from CassandraRepository
    // All return blocking types, not reactive
}