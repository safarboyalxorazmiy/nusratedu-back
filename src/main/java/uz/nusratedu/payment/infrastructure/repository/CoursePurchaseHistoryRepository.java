package uz.nusratedu.payment.infrastructure.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;
import uz.nusratedu.payment.infrastructure.entity.CoursePurchaseHistoryEntity;

import java.util.UUID;

@Repository
public interface CoursePurchaseHistoryRepository extends ReactiveCassandraRepository<CoursePurchaseHistoryEntity, UUID> {
    @AllowFiltering
    Mono<Boolean> existsByUserIdAndCourseId(String userId, String courseId);

    Flux<CoursePurchaseHistoryEntity> findByUserId(String telegramId);
}