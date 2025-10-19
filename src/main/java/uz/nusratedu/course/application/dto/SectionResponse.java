package uz.nusratedu.course.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionResponse {
    private UUID id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("course_id")
    private UUID courseId;
}