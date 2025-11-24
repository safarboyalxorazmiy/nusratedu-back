package uz.nusratedu.test.infastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import uz.nusratedu.test.infastructure.entity.TestQuestionEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestQuestionRepository extends CassandraRepository<TestQuestionEntity, UUID> {

    @Query("SELECT * FROM test_question WHERE test_id = ?0 ALLOW FILTERING")
    List<TestQuestionEntity> findByTestId(UUID testId);

    // Optional: if you want to partition by lesson_id (better performance)
    // @Query("SELECT * FROM test_question WHERE lesson_id = ?0 ALLOW FILTERING")
    // List<TestQuestionEntity> findByLessonId(UUID lessonId);
}