package uz.nusratedu.util.payme;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class PaymeServiceImpl implements PaymeService {

    private static final String PAYME_API_URL = "https://checkout.test.paycom.uz/api";
    private static final String AUTH_HEADER = "X-auth";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String JSON_TYPE = "application/json; charset=UTF-8";

    private static final String DEFAULT_AUTH = "5e730e8e0b852a417aa49ceb:ZPDODSiTYKuX0jyO7Kl2to4rQbNwG08jbghj";

    @Override
    public String createCard(String cardNumber, String expireDate, Long id, String authToken) {
        JSONObject body = new JSONObject()
                .put("id", id)
                .put("method", "cards.create")
                .put("params", new JSONObject()
                        .put("card", new JSONObject()
                                .put("number", cardNumber)
                                .put("expire", expireDate))
                        .put("save", false));

        JSONObject response = sendRequest(body, authToken);
        JSONObject card = response.optJSONObject("result") != null
                ? response.getJSONObject("result").optJSONObject("card")
                : null;

        if (card != null && card.has("token")) {
            return card.getString("token");
        }
        throw new CardNotFoundException("Card token not found");
    }

    @Override
    public String getVerifyCode(String token, Long id, String authToken) {
        JSONObject body = new JSONObject()
                .put("id", id)
                .put("method", "cards.get_verify_code")
                .put("params", new JSONObject().put("token", token));

        JSONObject response = sendRequest(body, authToken);
        return response.getJSONObject("result").optString("phone", "UNKNOWN");
    }

    @Override
    public boolean verifyCard(String token, String code, int id, String authToken) {
        JSONObject body = new JSONObject()
                .put("id", id)
                .put("method", "cards.verify")
                .put("params", new JSONObject()
                        .put("token", token)
                        .put("code", code));

        JSONObject response = sendRequest(body, authToken);
        return response.optJSONObject("result") != null &&
                response.getJSONObject("result")
                        .optJSONObject("card") != null &&
                response.getJSONObject("result")
                        .getJSONObject("card")
                        .optBoolean("verify", false);
    }

    @Override
    public String createReceipt(int amount, int orderId, String title, int price,
                                int count, String code, int vatPercent, String packageCode) {

        JSONObject item = new JSONObject()
                .put("title", title)
                .put("price", price)
                .put("count", count)
                .put("code", code)
                .put("vat_percent", vatPercent)
                .put("package_code", packageCode);

        JSONObject params = new JSONObject()
                .put("amount", amount)
                .put("order_id", orderId)
                .put("detail", new JSONObject()
                        .put("receipt_type", 0)
                        .put("items", new JSONArray().put(item)));

        JSONObject body = new JSONObject()
                .put("id", 4)
                .put("method", "receipts.create")
                .put("params", params);

        JSONObject response = sendRequest(body, DEFAULT_AUTH);
        return response.getJSONObject("result")
                .getJSONObject("receipt")
                .getString("_id");
    }

    @Override
    public String payReceipt(String receiptId, String token) {
        JSONObject body = new JSONObject()
                .put("id", 123)
                .put("method", "receipts.pay")
                .put("params", new JSONObject()
                        .put("id", receiptId)
                        .put("token", token));

        JSONObject response = sendRequest(body, DEFAULT_AUTH);
        return response.getJSONObject("result")
                .getJSONObject("receipt")
                .getJSONObject("payer")
                .getString("phone");
    }

    // Reusable HTTP request logic
    private JSONObject sendRequest(JSONObject body, String authToken) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(PAYME_API_URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty(AUTH_HEADER, authToken);
            connection.setRequestProperty(CONTENT_TYPE, JSON_TYPE);
            connection.setDoOutput(true);

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8")) {
                writer.write(body.toString());
                writer.flush();
            }

            int responseCode = connection.getResponseCode();
            InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("error")) {
                log.error("Payme API Error: {}", jsonResponse);
                throw new PaymeServerErrorException("PAYME_ERROR");
            }

            return jsonResponse;
        } catch (Exception e) {
            log.error("Payme request failed", e);
            throw new PaymeServerErrorException("PAYME_ERROR");
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
