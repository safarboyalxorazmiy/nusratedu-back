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

@Table("test_answer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswerEntity {
    @PrimaryKey
    @Builder.Default
    private UUID id = Uuids.timeBased();

    @Column("question_id")
    private UUID questionId;

    @Column("answer_text")
    private String answerText;

    @Column("is_correct")
    private Boolean isCorrect;
}