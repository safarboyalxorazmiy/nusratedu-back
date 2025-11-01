package uz.nusratedu.course.infrastructure.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.util.UUID;

@Table("completed_lesson")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletedLessonEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column("user_id")
    @Indexed
    private String userId;

    @Column("lesson_id")
    private UUID lessonId;

    @Column("completed_at")
    private Instant completedAt;
}