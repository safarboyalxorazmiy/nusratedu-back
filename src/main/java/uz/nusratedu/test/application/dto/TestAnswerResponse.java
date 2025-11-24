package uz.nusratedu.test.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswerResponse {
    private UUID id;
    private UUID questionId;
    private String answerText;
    private boolean isCorrect;
}