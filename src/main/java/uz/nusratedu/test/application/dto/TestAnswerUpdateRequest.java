package uz.nusratedu.test.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswerUpdateRequest {
    private String answerText;
    private Boolean isCorrect;
}