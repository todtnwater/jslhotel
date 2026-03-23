package command.member;

import java.net.Authenticator.RequestorType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import common.CommonExecute;
import dto.ReservationDto;

public class ChangePasswordEmail {

	// ⚠️ 실제 Gmail 계정 정보로 변경 필요!
		 private static final String SMTP_HOST = "smtp.gmail.com"; 
		    private static final String SMTP_PORT = "587";
		    private static final String SENDER_EMAIL = "1026wls@gmail.com";
		    private static final String SENDER_PASSWORD = "XXXXXXXXXXXX";
		    private static final String SENDER_NAME = "XXXXXXXXXXXX";
	    
	    /**
	     * 비밀번호 변경 이메일 발송
	     */
	    public boolean sendChangePasswordEmail(HttpServletRequest request) {
	        
	        try {
	            
	            String id = request.getParameter("t_id");
	            String tempPassword = generateTempCode();
	            
	            // 1. 받는 사람 이메일
	            String toEmail = request.getParameter("t_email_1") + "@" + request.getParameter("t_email_2");
	            
	            // 3. 이메일 제목
	            String subject = "[JSL 호텔] 비밀번호 변경 - " + id + " 님";
	            
	            // 4. HTML 이메일 내용 생성
	            String htmlContent = createEmailContentKorean(id, tempPassword);
	            
	            // 5. Gmail SMTP 설정
	            Properties props = new Properties();
	            props.put("mail.smtp.host", SMTP_HOST);
	            props.put("mail.smtp.port", SMTP_PORT);
	            props.put("mail.smtp.auth", "true");
	            props.put("mail.smtp.starttls.enable", "true");
	            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
	            props.put("mail.smtp.ssl.trust", SMTP_HOST);
	            
	            // 6. 세션 생성
	            Session session = Session.getInstance(props, new Authenticator() {
	                @Override
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
	                }
	            });
	            
	            // 7. 메시지 생성
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME, "UTF-8"));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
	            message.setSubject(subject);
	            message.setContent(htmlContent, "text/html; charset=UTF-8");
	            
	            // 8. 이메일 발송
	            Transport.send(message);
	            
	            HttpSession tempSession = request.getSession();
	            tempSession.setAttribute("t_tempPassword", tempPassword);
	            
	            
	            return true;
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("❌ 이메일 발송 실패: " + e.getMessage());
	            return false;
	        }
	    }
	    
	    
	    /**
	     * 임시 비밀번호 생성
	     */
	    // 사용할 문자 셋과 길이 상수를 클래스 멤버로 정의
	    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
	    private static final int CODE_LENGTH = 8;
	    
	    // 임시 비밀번호 생성
	    private static String generateTempCode() {
	        StringBuilder sb = new StringBuilder(CODE_LENGTH); 
	        int charactersLength = CHARACTERS.length();
	        
	        for (int i = 0; i < CODE_LENGTH; i++) {
	            // ThreadLocalRandom을 사용하여 난수 생성
	            int randomIndex = ThreadLocalRandom.current().nextInt(charactersLength);
	            char randomChar = CHARACTERS.charAt(randomIndex);
	            sb.append(randomChar);
	        }
	        
	        return sb.toString();
	    }

	    
	    /**
	     * 한국어 HTML 이메일 내용 생성
	     */
	    private String createEmailContentKorean(String id, String password) {
	        
	        
	        // HTML 템플릿 (프로젝트 색상 적용)
	        return "<!DOCTYPE html>" +
	"<html lang=\"ko\">" +
	"<head><meta charset=\"UTF-8\"><title> [JSL HOTEL] "+id+" 님 비밀번호 변경</title></head>" +
	"<body style=\"margin:0;padding:0;font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;background-color:#EEF7FF;\">" +
	"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#EEF7FF;padding:40px 0;\">" +
	"<tr><td align=\"center\">" +
	"<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff;border-radius:15px;box-shadow:0 5px 20px rgba(0,0,0,0.1);overflow:hidden;\">" +

	"<!-- 헤더 (프로젝트 색상) -->" +
	"<tr><td style=\"background:linear-gradient(135deg, #4D869C 0%, #7AB2B2 100%);padding:40px 30px;text-align:center;\">" +
	"<h1 style=\"margin:0;color:#ffffff;font-size:32px;font-weight:bold;letter-spacing:1px;\">🏨 JSL HOTEL</h1>" +
	"<p style=\"margin:10px 0 0 0;color:#ffffff;font-size:16px;opacity:0.95;\">임시 비밀번호를 전달드립니다.!</p>" +
	"</td></tr>" +

	"<!-- 비밀번호 -->" +
	"<tr><td style=\"padding:30px;background-color:#F7F4EA;text-align:center;\">" +
	"<p style=\"margin:0 0 10px 0;color:#666;font-size:14px;font-weight:600;\">비밀번호</p>" +
	"<h2 style=\"margin:0;color:#4D869C;font-size:28px;font-weight:bold;letter-spacing:3px;\">" + password + "</h2>" +
	"</td></tr>" +

	"<!-- 푸터 -->" +
	"<tr><td style=\"padding:30px;background:linear-gradient(to bottom, #EEF7FF, #4D869C);text-align:center;\">" +
	"<p style=\"margin:0 0 10px 0;color:#ffffff;font-size:14px;\">문의사항이 있으시면 언제든지 연락주세요</p>" +
	"<p style=\"margin:0;color:#ffffff;font-size:13px;opacity:0.9;\">📞 1588-1234 | ✉️ support@jslhotel.com</p>" +
	"<p style=\"margin:15px 0 0 0;color:#ffffff;font-size:12px;opacity:0.8;\">© 2025 JSL Hotel. All rights reserved.</p>" +
	"</td></tr>" +

	"</table></td></tr></table></body></html>";
	    }
	    
	    
	}
