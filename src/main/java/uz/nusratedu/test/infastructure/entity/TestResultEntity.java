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

@Table("test_result")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column("test_id")
    private UUID testId;

    @Column("user_id")
    private String userId;

    @Column("started_at")
    private LocalDateTime startedAt;

    @Column("finished_at")
    private LocalDateTime finishedAt;

    @Column("is_passed")
    private Boolean isPassed;

    @Column("score")
    private Integer score;

    @Column("attempt_number")
    private Integer attemptNumber;

    @Column("created_at")
    private LocalDateTime createdAt;
}