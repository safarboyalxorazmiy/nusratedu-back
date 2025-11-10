package uz.nusratedu.course.infrastructure.repository;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.course.infrastructure.entity.GiftByOwnerEntity;

import java.util.UUID;

@Repository
public interface GiftByOwnerRepository extends ReactiveCassandraRepository<GiftByOwnerEntity, UUID> {
}