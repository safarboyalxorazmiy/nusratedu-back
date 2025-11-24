package uz.nusratedu.test.infastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import uz.nusratedu.test.infastructure.entity.TestResultEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestResultRepository extends CassandraRepository<TestResultEntity, UUID> {

    @Query("SELECT * FROM test_result WHERE user_id = ?0 ALLOW FILTERING")
    List<TestResultEntity> findByUserId(String userTelegramId);

    @Query("SELECT * FROM test_result WHERE test_id = ?0 ALLOW FILTERING")
    List<TestResultEntity> findByTestId(UUID testId);

    @Query("SELECT * FROM test_result WHERE test_id = ?0 AND user_id = ?1 ALLOW FILTERING")
    List<TestResultEntity> findByTestIdAndUserId(UUID testId, String userTelegramId);
}