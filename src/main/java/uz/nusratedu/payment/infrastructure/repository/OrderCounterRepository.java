package uz.nusratedu.payment.infrastructure.repository;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import uz.nusratedu.payment.infrastructure.entity.OrderCounter;

@Repository
public interface OrderCounterRepository extends CassandraRepository<OrderCounter, String> {

    @Query("UPDATE order_counter SET value = value + 1 WHERE id = :id")
    void increment(String id);

    @Query("SELECT value FROM order_counter WHERE id = :id")
    Long getValue(String id);
}
