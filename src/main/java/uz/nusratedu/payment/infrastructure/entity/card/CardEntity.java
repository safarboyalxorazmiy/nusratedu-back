package uz.nusratedu.payment.infrastructure.entity.card;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("card_entity")
public class CardEntity {

    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    private Long orderId;
    private String number;
    private String expire;
    private String token = "";
    private String userId;

    @Column("created_at")
    @Builder.Default
    private LocalDateTime createdAt =  LocalDateTime.now();
}