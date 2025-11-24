package uz.nusratedu.test.infastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import uz.nusratedu.test.infastructure.entity.TestAnswerResultEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestAnswerResultRepository extends CassandraRepository<TestAnswerResultEntity, UUID> {

    @Query("SELECT * FROM test_answer_result WHERE result_id = ?0 ALLOW FILTERING")
    List<TestAnswerResultEntity> findByResultId(Integer resultId);

    @Query("SELECT * FROM test_answer_result WHERE result_id = ?0 AND question_id = ?1 ALLOW FILTERING")
    List<TestAnswerResultEntity> findByResultIdAndQuestionId(Integer resultId, Integer questionId);
}