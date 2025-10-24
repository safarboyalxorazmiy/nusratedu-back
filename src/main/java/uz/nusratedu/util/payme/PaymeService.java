package uz.nusratedu.util.payme;

public interface PaymeService {

    /**
     * Creates a Payme card and returns its token.
     */
    String createCard(String cardNumber, String expireDate, Long id, String authToken);

    /**
     * Requests a verification code for a card token and returns the phone number tied to the card.
     */
    String getVerifyCode(String token, Long id, String authToken);

    /**
     * Verifies a card using the code sent to the user's phone.
     */
    boolean verifyCard(String token, String code, int id, String authToken);

    /**
     * Creates a new receipt and returns the receipt ID.
     */
    String createReceipt(
            int amount,
            int orderId,
            String title,
            int price,
            int count,
            String code,
            int vatPercent,
            String packageCode
    );

    /**
     * Pays for the receipt and returns payer's phone number.
     */
    String payReceipt(String receiptId, String token);
}
