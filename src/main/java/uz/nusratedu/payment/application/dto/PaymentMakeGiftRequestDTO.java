package uz.nusratedu.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMakeGiftRequestDTO {
    private String number;

    private String expire;

    @JsonProperty("course_id")
    private String courseId;
}