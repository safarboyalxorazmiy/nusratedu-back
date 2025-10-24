package uz.nusratedu.course.infrastructure.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("compleated_lesson")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedLessonEntity {
    @PrimaryKey
    private UUID id = Uuids.timeBased();

    @Column("user_id")
    private String userId;

    @Column("lesson_id")
    private UUID lessonId;
}