package uz.nusratedu.test.infastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import uz.nusratedu.test.infastructure.entity.TestAnswerEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestAnswerRepository extends CassandraRepository<TestAnswerEntity, UUID> {

    @Query("SELECT * FROM test_answer WHERE question_id = ?0 ALLOW FILTERING")
    List<TestAnswerEntity> findByQuestionId(UUID questionId);
}