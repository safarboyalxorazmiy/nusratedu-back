package uz.nusratedu.payment.infrastructure.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.payment.infrastructure.entity.CoursePurchaseHistoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CoursePurchaseHistoryRepository extends CassandraRepository<CoursePurchaseHistoryEntity, UUID> {

    @AllowFiltering
    Optional<CoursePurchaseHistoryEntity> findByUserIdAndCourseId(String userId, String courseId);

    List<CoursePurchaseHistoryEntity> findByUserId(String telegramId);
}