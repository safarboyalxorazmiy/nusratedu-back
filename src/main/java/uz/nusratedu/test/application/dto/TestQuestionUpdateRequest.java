package uz.nusratedu.test.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestionUpdateRequest {
    private String questionText;
    private String questionType;
    private Integer pointValue;
    private Integer orderIndex;
}