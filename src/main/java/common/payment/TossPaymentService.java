package common.payment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TossPaymentService {
    
    public PaymentResponse confirmPayment(String paymentKey, String orderId, int amount) {
        PaymentResponse response = new PaymentResponse();
        
        try {
            String secretKey = TossPaymentConfig.getSecretKey();
            String encodedAuth = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
            
            URL url = new URL(TossPaymentConfig.TOSS_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("paymentKey", paymentKey);
            requestBody.addProperty("orderId", orderId);
            requestBody.addProperty("amount", amount);
            
            System.out.println("토스 API 요청: " + requestBody.toString());
            
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = connection.getResponseCode();
            System.out.println("토스 API 응답 코드: " + responseCode);
            
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(
                    connection.getErrorStream(), StandardCharsets.UTF_8));
            }
            
            StringBuilder responseBody = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseBody.append(responseLine.trim());
            }
            br.close();
            
            System.out.println("토스 API 응답: " + responseBody.toString());
            
            if (responseCode == 200) {
                JsonObject jsonResponse = JsonParser.parseString(responseBody.toString())
                    .getAsJsonObject();
                
                response.setSuccess(true);
                response.setStatus(jsonResponse.get("status").getAsString());
                response.setMethod(jsonResponse.get("method").getAsString());
                response.setOrderId(jsonResponse.get("orderId").getAsString());
                response.setPaymentKey(paymentKey);
                response.setAmount(amount);
                response.setRawResponse(responseBody.toString());
            } else {
                response.setSuccess(false);
                response.setMessage("결제 승인 실패");
                response.setRawResponse(responseBody.toString());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("결제 승인 중 오류: " + e.getMessage());
        }
        
        return response;
    }
    
    public PaymentResponse cancelPayment(String paymentKey, String cancelReason) {
        PaymentResponse response = new PaymentResponse();
        
        try {
            String secretKey = TossPaymentConfig.getSecretKey();
            String encodedAuth = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
            
            String cancelUrl = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";
            URL url = new URL(cancelUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("cancelReason", cancelReason);
            
            System.out.println("토스 취소 요청: " + requestBody.toString());
            
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = connection.getResponseCode();
            System.out.println("토스 취소 응답 코드: " + responseCode);
            
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(
                    connection.getErrorStream(), StandardCharsets.UTF_8));
            }
            
            StringBuilder responseBody = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseBody.append(responseLine.trim());
            }
            br.close();
            
            System.out.println("토스 취소 응답: " + responseBody.toString());
            
            if (responseCode == 200) {
                response.setSuccess(true);
                response.setMessage("결제가 취소되었습니다");
                response.setRawResponse(responseBody.toString());
            } else {
                response.setSuccess(false);
                response.setMessage("결제 취소 실패");
                response.setRawResponse(responseBody.toString());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage("결제 취소 중 오류: " + e.getMessage());
        }
        
        return response;
    }
}