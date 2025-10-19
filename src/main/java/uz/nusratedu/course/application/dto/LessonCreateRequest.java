package uz.nusratedu.course.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonCreateRequest {
    @JsonProperty("title")
    private String title;

    @JsonProperty("vimeo_url")
    private String vimeoUrl;

    @JsonProperty("pdf_url")
    private String pdfUrl;

    @JsonProperty("section_id")
    private UUID sectionId;
}