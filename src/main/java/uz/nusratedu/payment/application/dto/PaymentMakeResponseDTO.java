package uz.nusratedu.payment.application.dto;

import lombok.Data;

@Data
public class PaymentMakeResponseDTO {
    private String token;
    private String phone;
}