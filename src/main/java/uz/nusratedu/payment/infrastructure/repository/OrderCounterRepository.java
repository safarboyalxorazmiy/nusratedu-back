package uz.nusratedu.payment.infrastructure.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import uz.nusratedu.payment.infrastructure.entity.OrderCounter;

@Repository
public interface OrderCounterRepository extends CassandraRepository<OrderCounter, String> {
}