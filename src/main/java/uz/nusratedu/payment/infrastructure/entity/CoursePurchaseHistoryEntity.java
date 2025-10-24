package uz.nusratedu.payment.infrastructure.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Table("course_purchase_history")
public class CoursePurchaseHistoryEntity {


    @PrimaryKeyColumn(name = "purchase_id", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID purchaseId = Uuids.timeBased();

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @Column("course_id")
    private String courseId;

    @Column("price")
    private BigDecimal price;

    @Column("currency")
    private String currency;

    @Column("payment_method")
    private String paymentMethod;

    @Column("purchased_at")
    private Instant purchasedAt;

    @Column("status")
    private String status;

}