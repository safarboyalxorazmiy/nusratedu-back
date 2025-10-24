package uz.nusratedu.payment.infrastructure.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;
import uz.nusratedu.payment.infrastructure.entity.CoursePurchaseHistoryEntity;

import java.util.UUID;

@Repository
public interface CoursePurchaseHistoryRepository extends ReactiveCassandraRepository<CoursePurchaseHistoryEntity, UUID> {
}