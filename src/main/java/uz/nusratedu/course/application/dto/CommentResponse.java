package uz.nusratedu.course.application.dto;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private UUID id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("lesson_id")
    private UUID lessonId;
}
