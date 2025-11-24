package uz.nusratedu.test.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponse {
    private UUID id;
    private UUID testId;
    private String userId; // Telegram ID as String
    private Integer score;
    private Boolean isPassed;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Integer attemptNumber;
    private LocalDateTime createdAt;
}