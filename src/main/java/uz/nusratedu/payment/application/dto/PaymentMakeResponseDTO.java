package uz.nusratedu.payment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMakeResponseDTO {
    private String token;
    private String phone;
}