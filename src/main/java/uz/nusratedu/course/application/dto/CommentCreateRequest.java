package uz.nusratedu.course.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentCreateRequest {
    @JsonProperty("content")
    private String content;

    @JsonProperty("lesson_id")
    private UUID lessonId;
}