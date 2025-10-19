package uz.nusratedu.course.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseCreateRequest {
    @JsonProperty("course_name")
    private String courseName;

    @JsonProperty("course_description")
    private String courseDescription;

    @JsonProperty("course_status")
    private String courseStatus;

    @JsonProperty("course_field")
    private String courseField;
}