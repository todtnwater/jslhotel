package command.payment;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;

import common.CommonExecute;
import common.payment.PaymentResponse;
import common.payment.TossPaymentService;

public class ConfirmPayment implements CommonExecute {

    @Override
    public void execute(HttpServletRequest request) {
        System.out.println("=== ConfirmPayment 실행 ===");

        try {
            JsonObject jsonObj = (JsonObject) request.getAttribute("jsonObject");

            String paymentKey = jsonObj.get("paymentKey").getAsString();
            String orderId = jsonObj.get("orderId").getAsString();
            int amount = jsonObj.get("amount").getAsInt();

            System.out.println("paymentKey: " + paymentKey);
            System.out.println("orderId: " + orderId);
            System.out.println("amount: " + amount);

            TossPaymentService tossService = new TossPaymentService();
            PaymentResponse paymentResponse = tossService.confirmPayment(paymentKey, orderId, amount);

            if (paymentResponse.isSuccess()) {
                JsonObject successResponse = new JsonObject();
                successResponse.addProperty("success", true);
                successResponse.addProperty("status", paymentResponse.getStatus());
                successResponse.addProperty("method", paymentResponse.getMethod());
                successResponse.addProperty("orderId", paymentResponse.getOrderId());
                successResponse.addProperty("paymentKey", paymentResponse.getPaymentKey());
                successResponse.addProperty("amount", paymentResponse.getAmount());

                request.setAttribute("jsonResult", successResponse.toString());
                request.setAttribute("paymentResponse", paymentResponse);
            } else {
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("success", false);
                errorResponse.addProperty("message", paymentResponse.getMessage());
                errorResponse.addProperty("error", paymentResponse.getRawResponse());

                request.setAttribute("jsonResult", errorResponse.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ConfirmPayment 오류: " + e.getMessage());

            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "결제 승인 중 오류 발생: " + e.getMessage());
            request.setAttribute("jsonResult", errorResponse.toString());
        }
    }
}
