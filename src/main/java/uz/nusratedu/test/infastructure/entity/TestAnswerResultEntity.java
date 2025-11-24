package uz.nusratedu.test.infastructure.entity;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("test_answer_result")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswerResultEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column("selected_answer_id")
    private Integer selectedAnswerId;

    @Column("question_id")
    private Integer questionId;

    @Column("result_id")
    private Integer resultId;

    @Column("is_correct")
    private Boolean isCorrect;
}