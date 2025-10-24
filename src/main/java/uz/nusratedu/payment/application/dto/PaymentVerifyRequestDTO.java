package uz.nusratedu.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentVerifyRequestDTO {
    private String token;

    private String code;

    @JsonProperty("course_id")
    private String courseId;
}