package uz.nusratedu.payment.infrastructure.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("order_counter")
public class OrderCounter {

    @PrimaryKey
    private String id;

    private Long value;
}