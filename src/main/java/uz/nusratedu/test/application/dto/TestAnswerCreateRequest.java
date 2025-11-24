package uz.nusratedu.test.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswerCreateRequest {

    @NotNull
    private UUID questionId;

    @NotBlank
    private String answerText;

    @Builder.Default
    private boolean isCorrect = false;
}