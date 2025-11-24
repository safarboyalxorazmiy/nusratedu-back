package uz.nusratedu.test.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSubmissionRequest {

    @NotNull
    private UUID resultId;

    /**
     * Map: questionId -> selectedAnswerId (Integer or List<Integer> for multi-select)
     * For simplicity, using Integer (single choice). Change to List<Integer> if needed.
     */
    @NotEmpty
    private Map<UUID, Integer> answers;
}