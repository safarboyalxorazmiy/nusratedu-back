package uz.nusratedu.course.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompletedLessonResponse {
    private UUID id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("lesson_id")
    private String lessonId;
}