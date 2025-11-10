package uz.nusratedu.course.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.UUID;

@Table("gift_by_owner")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftByOwnerEntity {

    @PrimaryKeyClass
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key {
        @PrimaryKeyColumn(name = "owner_id", type = PrimaryKeyType.PARTITIONED)
        private String ownerId;

        @PrimaryKeyColumn(name = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private UUID id; // time-based UUID for ordering
    }

    @PrimaryKey
    private Key key;

    @Column("course_id")
    private UUID courseId;

    @Column("member_id")
    private String memberId;
}
