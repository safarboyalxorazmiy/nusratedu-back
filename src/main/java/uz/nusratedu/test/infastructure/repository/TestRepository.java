package uz.nusratedu.test.infastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import uz.nusratedu.test.infastructure.entity.TestEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestRepository extends CassandraRepository<TestEntity, UUID> {

    @Query("SELECT * FROM test WHERE lesson_id = ?0 ALLOW FILTERING")
    List<TestEntity> findByLessonId(UUID lessonId);
}