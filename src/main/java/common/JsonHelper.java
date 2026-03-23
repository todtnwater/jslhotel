package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonHelper {
    
    public static JsonObject readJsonBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        String jsonBody = sb.toString();
        System.out.println(">>> 받은 JSON 데이터: " + jsonBody);
        
        if (jsonBody == null || jsonBody.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON 데이터가 비어있음");
        }
        
        JsonObject jsonObj = JsonParser.parseString(jsonBody).getAsJsonObject();
        
        System.out.println(">>> JSON 파싱 완료");
        System.out.println(">>> JSON 필드: " + jsonObj.keySet());
        
        return jsonObj;
    }
    
    public static void sendJsonResponse(HttpServletResponse response, String jsonResult) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(jsonResult);
        out.flush();
    }
    
    public static void sendErrorJson(HttpServletResponse response, String message) throws IOException {
        String errorJson = "{\"success\":false,\"message\":\"" + message + "\"}";
        sendJsonResponse(response, errorJson);
    }
    
    public static boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains("application/json");
    }
}
