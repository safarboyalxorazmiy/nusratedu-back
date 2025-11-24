package uz.nusratedu.test.infastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.test.infastructure.entity.TestEntity;

import java.util.UUID;

@Repository
public interface TestRepository extends CassandraRepository<TestEntity, UUID> {
}