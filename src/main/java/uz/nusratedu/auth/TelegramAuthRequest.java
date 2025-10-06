package uz.nusratedu.auth;

import lombok.Data;

@Data
public class TelegramAuthRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private long authDate;
    private String hash;
}
