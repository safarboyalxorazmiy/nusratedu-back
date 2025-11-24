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

@Table("test_question")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestionEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column("lesson_id")
    private UUID lessonId;

    @Column("question_text")
    private String questionText;

    @Column("question_type")
    private String questionType;

    @Column("point_value")
    private Integer pointValue;

    @Column("order_index")
    private Integer orderIndex;

    @Column("test_id")
    private UUID testId;
}
