package uz.nusratedu.test.infastructure.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("test")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column("lesson_id")
    private UUID lessonId;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("total_questions")
    private Long totalQuestions;

    @Column("passing_score")
    private Long passingScore;

    @Column("max_score")
    private Long maxScore;

    @Column("created_at")
    private LocalDateTime createdAt;
}