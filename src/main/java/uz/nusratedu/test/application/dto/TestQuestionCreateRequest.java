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
public class TestQuestionCreateRequest {

    @NotNull
    private UUID testId;

    @NotNull
    private UUID lessonId;

    @NotBlank
    private String questionText;

    @NotBlank
    private String questionType; // e.g., "MULTIPLE_CHOICE", "TRUE_FALSE", "TEXT"

    @Builder.Default
    private Integer pointValue = 1;

    @Builder.Default
    private Integer orderIndex = 0;
}