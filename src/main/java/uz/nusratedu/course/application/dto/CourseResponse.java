package uz.nusratedu.course.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse {
    private UUID id;

    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_description")
    private String courseDescription;

    @JsonProperty("course_status")
    private String courseStatus;

    @JsonProperty("course_field")
    private String courseField;

    @JsonProperty
    private Boolean purchased;

    @JsonProperty("purchased_at")
    private Instant purchasedAt;

    @JsonProperty("course_attach_url")
    private String courseAttachUrl;
}
