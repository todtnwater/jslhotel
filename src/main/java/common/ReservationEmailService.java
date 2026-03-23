package common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dto.ReservationDto;

/**
 * 예약 확인 이메일 발송 서비스 (한/영 다국어 지원, 프로젝트 디자인 적용)
 */
public class ReservationEmailService {
    
    // email
	 private static final String SMTP_HOST = "smtp.gmail.com"; 
	    private static final String SMTP_PORT = "587";
	    private static final String SENDER_EMAIL = "1026wls@gmail.com";
	    private static final String SENDER_PASSWORD = "XXXXXXXXXXXXXX";
	    private static final String SENDER_NAME = "XXXXXXXXX";
    
    /**
     * 예약 확인 이메일 발송
     */
    public boolean sendReservationConfirmEmail(ReservationDto reservation) {
        
        try {
            System.out.println("========== 예약 확인 이메일 발송 시작 ==========");
            
            // 1. 받는 사람 이메일
            String toEmail = reservation.getRv_customer_email_1() + "@" + reservation.getRv_customer_email_2();
            
            // 2. 국가별 언어 선택
            String country = reservation.getRv_country();
            boolean isKorean = "korea".equalsIgnoreCase(country);
            
            // 3. 이메일 제목
            String subject;
            if (isKorean) {
                subject = "[JSL 호텔] 예약이 완료되었습니다 - " + reservation.getRv_no();
            } else {
                subject = "[JSL Hotel] Reservation Confirmed - " + reservation.getRv_no();
            }
            
            // 4. HTML 이메일 내용 생성
            String htmlContent;
            if (isKorean) {
                htmlContent = createEmailContentKorean(reservation);
            } else {
                htmlContent = createEmailContentEnglish(reservation);
            }
            
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
            
            System.out.println(" 예약 확인 이메일 발송 완료 (" + (isKorean ? "한국어" : "English") + ")");
            System.out.println("   수신자: " + toEmail);
            System.out.println("=========================================");
            
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" 이메일 발송 실패: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 한국어 HTML 이메일 내용 생성
     */
    private String createEmailContentKorean(ReservationDto reservation) {
        
        // 데이터 준비
        LocalDate checkIn = LocalDate.parse(reservation.getRv_check_in_date());
        LocalDate checkOut = LocalDate.parse(reservation.getRv_check_out_date());
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String checkInFormatted = checkIn.format(formatter);
        String checkOutFormatted = checkOut.format(formatter);
        
        String customerName = reservation.getRv_customer_first_name() + " " + reservation.getRv_customer_last_name();
        String customerEmail = reservation.getRv_customer_email_1() + "@" + reservation.getRv_customer_email_2();
        String customerPhone = reservation.getRv_customer_mobile_1() + "-" + 
                               reservation.getRv_customer_mobile_2() + "-" + 
                               reservation.getRv_customer_mobile_3();
        
        String membershipLevel = reservation.getRv_membership_level();
        if (membershipLevel == null || membershipLevel.isEmpty() || membershipLevel.equals("GUEST")) {
            membershipLevel = "일반";
        }
        String membershipColor = getMembershipColor(membershipLevel);
        
        int originalPrice = reservation.getRv_original_price();
        int discountAmount = reservation.getRv_discount_amount();
        int finalPrice = reservation.getRv_total_price();
        
        double discountRate = 0;
        if (originalPrice > 0 && discountAmount > 0) {
            discountRate = (double) discountAmount / originalPrice * 100;
        }
        
        String discountDisplay = (discountAmount > 0) ? "" : "display: none;";
        
        String requestMessage = reservation.getRv_request_message();
        String requestDisplay = (requestMessage != null && !requestMessage.isEmpty()) ? "" : "display: none;";
        if (requestMessage == null) requestMessage = "";
        
        // HTML 템플릿 (프로젝트 색상 적용)
        return "<!DOCTYPE html>" +
"<html lang=\"ko\">" +
"<head><meta charset=\"UTF-8\"><title>JSL 호텔 예약 확인</title></head>" +
"<body style=\"margin:0;padding:0;font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;background-color:#EEF7FF;\">" +
"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#EEF7FF;padding:40px 0;\">" +
"<tr><td align=\"center\">" +
"<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff;border-radius:15px;box-shadow:0 5px 20px rgba(0,0,0,0.1);overflow:hidden;\">" +

"<!-- 헤더 (프로젝트 색상) -->" +
"<tr><td style=\"background:linear-gradient(135deg, #4D869C 0%, #7AB2B2 100%);padding:40px 30px;text-align:center;\">" +
"<h1 style=\"margin:0;color:#ffffff;font-size:32px;font-weight:bold;letter-spacing:1px;\">🏨 JSL HOTEL</h1>" +
"<p style=\"margin:10px 0 0 0;color:#ffffff;font-size:16px;opacity:0.95;\">예약이 완료되었습니다!</p>" +
"</td></tr>" +

"<!-- 예약 번호 -->" +
"<tr><td style=\"padding:30px;background-color:#F7F4EA;text-align:center;\">" +
"<p style=\"margin:0 0 10px 0;color:#666;font-size:14px;font-weight:600;\">예약번호</p>" +
"<h2 style=\"margin:0;color:#4D869C;font-size:28px;font-weight:bold;letter-spacing:3px;\">" + reservation.getRv_no() + "</h2>" +
"</td></tr>" +

"<!-- 예약자 정보 -->" +
"<tr><td style=\"padding:30px;\">" +
"<h3 style=\"margin:0 0 20px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">📋 예약자 정보</h3>" +
"<table width=\"100%\" cellpadding=\"8\" cellspacing=\"0\">" +
"<tr><td width=\"120\" style=\"color:#666;font-size:14px;padding:8px 0;\">예약자명</td><td style=\"color:#333;font-size:14px;font-weight:600;padding:8px 0;\">" + customerName + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">이메일</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + customerEmail + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">연락처</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + customerPhone + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">멤버십</td><td style=\"padding:8px 0;\">" +
"<span style=\"display:inline-block;padding:4px 12px;background-color:" + membershipColor + ";color:#ffffff;border-radius:4px;font-size:12px;font-weight:bold;\">" + membershipLevel + "</span>" +
"</td></tr>" +
"</table></td></tr>" +

"<!-- 객실 정보 -->" +
"<tr><td style=\"padding:0 30px 30px 30px;\">" +
"<h3 style=\"margin:0 0 20px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">🏠 객실 정보</h3>" +
"<table width=\"100%\" cellpadding=\"8\" cellspacing=\"0\">" +
"<tr><td width=\"120\" style=\"color:#666;font-size:14px;padding:8px 0;\">객실번호</td><td style=\"color:#333;font-size:14px;font-weight:600;padding:8px 0;\">" + reservation.getRv_room_no() + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">체크인</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + checkInFormatted + " <span style=\"color:#4D869C;font-weight:600;\">15:00</span></td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">체크아웃</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + checkOutFormatted + " <span style=\"color:#4D869C;font-weight:600;\">11:00</span></td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">투숙인원</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + reservation.getRv_guest_count() + "명</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">숙박일수</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + nights + "박</td></tr>" +
"</table></td></tr>" +

"<!-- 결제 정보 -->" +
"<tr><td style=\"padding:0 30px 30px 30px;\">" +
"<h3 style=\"margin:0 0 20px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">💳 결제 정보</h3>" +
"<table width=\"100%\" cellpadding=\"8\" cellspacing=\"0\" style=\"background-color:#F7F4EA;border-radius:10px;padding:10px;\">" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 15px;\">객실 요금</td><td align=\"right\" style=\"color:#333;font-size:14px;padding:8px 15px;\">" + String.format("%,d", originalPrice) + "원</td></tr>" +
"<tr style=\"" + discountDisplay + "\"><td style=\"color:#e74c3c;font-size:14px;padding:8px 15px;\">" + membershipLevel + " 할인 (" + String.format("%.0f", discountRate) + "%)</td><td align=\"right\" style=\"color:#e74c3c;font-size:14px;font-weight:600;padding:8px 15px;\">-" + String.format("%,d", discountAmount) + "원</td></tr>" +
"<tr><td colspan=\"2\" style=\"padding:8px 0;\"><div style=\"border-top:2px dashed #ddd;margin:10px 0;\"></div></td></tr>" +
"<tr><td style=\"color:#333;font-size:16px;font-weight:bold;padding:8px 15px;\">최종 결제금액</td><td align=\"right\" style=\"color:#4D869C;font-size:22px;font-weight:bold;padding:8px 15px;\">" + String.format("%,d", finalPrice) + "원</td></tr>" +
"</table></td></tr>" +

"<!-- 요청사항 -->" +
"<tr style=\"" + requestDisplay + "\"><td style=\"padding:0 30px 30px 30px;\">" +
"<h3 style=\"margin:0 0 15px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">📝 요청사항</h3>" +
"<div style=\"background-color:#fffacd;border-left:4px solid #ffc107;padding:15px;border-radius:4px;\">" +
"<p style=\"margin:0;color:#666;font-size:14px;line-height:1.6;\">" + requestMessage + "</p>" +
"</div></td></tr>" +

"<!-- 안내사항 -->" +
"<tr><td style=\"padding:30px;background-color:#F7F4EA;\">" +
"<h3 style=\"margin:0 0 15px 0;color:#333;font-size:16px;\">📌 체크인 안내</h3>" +
"<ul style=\"margin:0;padding-left:20px;color:#666;font-size:14px;line-height:1.8;\">" +
"<li>체크인: <strong>15:00</strong>부터 가능합니다.</li>" +
"<li>체크아웃: <strong>11:00</strong>까지 완료해 주세요.</li>" +
"<li>신분증(여권, 운전면허증 등)을 지참해 주세요.</li>" +
"<li>예약 번호를 프론트에서 말씀해 주세요.</li>" +
"<li>주차는 <strong>무료</strong>로 제공됩니다.</li>" +
"</ul></td></tr>" +

"<!-- 푸터 -->" +
"<tr><td style=\"padding:30px;background:linear-gradient(to bottom, #EEF7FF, #4D869C);text-align:center;\">" +
"<p style=\"margin:0 0 10px 0;color:#ffffff;font-size:14px;\">문의사항이 있으시면 언제든지 연락주세요</p>" +
"<p style=\"margin:0;color:#ffffff;font-size:13px;opacity:0.9;\">📞 1588-1234 | ✉️ info@luxuryhotel.com</p>" +
"<p style=\"margin:15px 0 0 0;color:#ffffff;font-size:12px;opacity:0.8;\">© 2025 LUXURY HOTEL. All rights reserved.</p>" +
"</td></tr>" +

"</table></td></tr></table></body></html>";
    }
    
    /**
     * 영어 HTML 이메일 내용 생성
     */
    private String createEmailContentEnglish(ReservationDto reservation) {
        
        // 데이터 준비
        LocalDate checkIn = LocalDate.parse(reservation.getRv_check_in_date());
        LocalDate checkOut = LocalDate.parse(reservation.getRv_check_out_date());
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String checkInFormatted = checkIn.format(formatter);
        String checkOutFormatted = checkOut.format(formatter);
        
        String customerName = reservation.getRv_customer_first_name() + " " + reservation.getRv_customer_last_name();
        String customerEmail = reservation.getRv_customer_email_1() + "@" + reservation.getRv_customer_email_2();
        String customerPhone = reservation.getRv_customer_mobile_1() + "-" + 
                               reservation.getRv_customer_mobile_2() + "-" + 
                               reservation.getRv_customer_mobile_3();
        
        String membershipLevel = reservation.getRv_membership_level();
        if (membershipLevel == null || membershipLevel.isEmpty() || membershipLevel.equals("GUEST")) {
            membershipLevel = "General";
        }
        String membershipColor = getMembershipColor(membershipLevel);
        
        int originalPrice = reservation.getRv_original_price();
        int discountAmount = reservation.getRv_discount_amount();
        int finalPrice = reservation.getRv_total_price();
        
        double discountRate = 0;
        if (originalPrice > 0 && discountAmount > 0) {
            discountRate = (double) discountAmount / originalPrice * 100;
        }
        
        String discountDisplay = (discountAmount > 0) ? "" : "display: none;";
        
        String requestMessage = reservation.getRv_request_message();
        String requestDisplay = (requestMessage != null && !requestMessage.isEmpty()) ? "" : "display: none;";
        if (requestMessage == null) requestMessage = "";
        
        // HTML 템플릿 (영어 + 프로젝트 색상)
        return "<!DOCTYPE html>" +
"<html lang=\"en\">" +
"<head><meta charset=\"UTF-8\"><title>JSL Hotel Reservation Confirmation</title></head>" +
"<body style=\"margin:0;padding:0;font-family:'Segoe UI',Tahoma,Geneva,Verdana,sans-serif;background-color:#EEF7FF;\">" +
"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#EEF7FF;padding:40px 0;\">" +
"<tr><td align=\"center\">" +
"<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff;border-radius:15px;box-shadow:0 5px 20px rgba(0,0,0,0.1);overflow:hidden;\">" +

"<!-- Header -->" +
"<tr><td style=\"background:linear-gradient(135deg, #4D869C 0%, #7AB2B2 100%);padding:40px 30px;text-align:center;\">" +
"<h1 style=\"margin:0;color:#ffffff;font-size:32px;font-weight:bold;letter-spacing:1px;\">🏨 JSL HOTEL</h1>" +
"<p style=\"margin:10px 0 0 0;color:#ffffff;font-size:16px;opacity:0.95;\">Reservation Confirmed!</p>" +
"</td></tr>" +

"<!-- Reservation Number -->" +
"<tr><td style=\"padding:30px;background-color:#F7F4EA;text-align:center;\">" +
"<p style=\"margin:0 0 10px 0;color:#666;font-size:14px;font-weight:600;\">Reservation Number</p>" +
"<h2 style=\"margin:0;color:#4D869C;font-size:28px;font-weight:bold;letter-spacing:3px;\">" + reservation.getRv_no() + "</h2>" +
"</td></tr>" +

"<!-- Guest Information -->" +
"<tr><td style=\"padding:30px;\">" +
"<h3 style=\"margin:0 0 20px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">📋 Guest Information</h3>" +
"<table width=\"100%\" cellpadding=\"8\" cellspacing=\"0\">" +
"<tr><td width=\"120\" style=\"color:#666;font-size:14px;padding:8px 0;\">Name</td><td style=\"color:#333;font-size:14px;font-weight:600;padding:8px 0;\">" + customerName + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">Email</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + customerEmail + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">Phone</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + customerPhone + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">Membership</td><td style=\"padding:8px 0;\">" +
"<span style=\"display:inline-block;padding:4px 12px;background-color:" + membershipColor + ";color:#ffffff;border-radius:4px;font-size:12px;font-weight:bold;\">" + membershipLevel + "</span>" +
"</td></tr>" +
"</table></td></tr>" +

"<!-- Room Information -->" +
"<tr><td style=\"padding:0 30px 30px 30px;\">" +
"<h3 style=\"margin:0 0 20px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">🏠 Room Information</h3>" +
"<table width=\"100%\" cellpadding=\"8\" cellspacing=\"0\">" +
"<tr><td width=\"120\" style=\"color:#666;font-size:14px;padding:8px 0;\">Room No.</td><td style=\"color:#333;font-size:14px;font-weight:600;padding:8px 0;\">" + reservation.getRv_room_no() + "</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">Check-in</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + checkInFormatted + " <span style=\"color:#4D869C;font-weight:600;\">3:00 PM</span></td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">Check-out</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + checkOutFormatted + " <span style=\"color:#4D869C;font-weight:600;\">11:00 AM</span></td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">Guests</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + reservation.getRv_guest_count() + " person(s)</td></tr>" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 0;\">Nights</td><td style=\"color:#333;font-size:14px;padding:8px 0;\">" + nights + " night(s)</td></tr>" +
"</table></td></tr>" +

"<!-- Payment Information -->" +
"<tr><td style=\"padding:0 30px 30px 30px;\">" +
"<h3 style=\"margin:0 0 20px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">💳 Payment Details</h3>" +
"<table width=\"100%\" cellpadding=\"8\" cellspacing=\"0\" style=\"background-color:#F7F4EA;border-radius:10px;padding:10px;\">" +
"<tr><td style=\"color:#666;font-size:14px;padding:8px 15px;\">Room Rate</td><td align=\"right\" style=\"color:#333;font-size:14px;padding:8px 15px;\">₩" + String.format("%,d", originalPrice) + "</td></tr>" +
"<tr style=\"" + discountDisplay + "\"><td style=\"color:#e74c3c;font-size:14px;padding:8px 15px;\">" + membershipLevel + " Discount (" + String.format("%.0f", discountRate) + "%)</td><td align=\"right\" style=\"color:#e74c3c;font-size:14px;font-weight:600;padding:8px 15px;\">-₩" + String.format("%,d", discountAmount) + "</td></tr>" +
"<tr><td colspan=\"2\" style=\"padding:8px 0;\"><div style=\"border-top:2px dashed #ddd;margin:10px 0;\"></div></td></tr>" +
"<tr><td style=\"color:#333;font-size:16px;font-weight:bold;padding:8px 15px;\">Total Amount</td><td align=\"right\" style=\"color:#4D869C;font-size:22px;font-weight:bold;padding:8px 15px;\">₩" + String.format("%,d", finalPrice) + "</td></tr>" +
"</table></td></tr>" +

"<!-- Special Requests -->" +
"<tr style=\"" + requestDisplay + "\"><td style=\"padding:0 30px 30px 30px;\">" +
"<h3 style=\"margin:0 0 15px 0;color:#333;font-size:18px;border-bottom:2px solid #4D869C;padding-bottom:10px;\">📝 Special Requests</h3>" +
"<div style=\"background-color:#fffacd;border-left:4px solid #ffc107;padding:15px;border-radius:4px;\">" +
"<p style=\"margin:0;color:#666;font-size:14px;line-height:1.6;\">" + requestMessage + "</p>" +
"</div></td></tr>" +

"<!-- Check-in Guide -->" +
"<tr><td style=\"padding:30px;background-color:#F7F4EA;\">" +
"<h3 style=\"margin:0 0 15px 0;color:#333;font-size:16px;\">📌 Check-in Information</h3>" +
"<ul style=\"margin:0;padding-left:20px;color:#666;font-size:14px;line-height:1.8;\">" +
"<li>Check-in starts at <strong>3:00 PM</strong></li>" +
"<li>Check-out by <strong>11:00 AM</strong></li>" +
"<li>Please bring a valid ID (passport, driver's license)</li>" +
"<li>Quote your reservation number at the front desk</li>" +
"<li>Complimentary <strong>parking</strong> available</li>" +
"</ul></td></tr>" +

"<!-- Footer -->" +
"<tr><td style=\"padding:30px;background:linear-gradient(to bottom, #EEF7FF, #4D869C);text-align:center;\">" +
"<p style=\"margin:0 0 10px 0;color:#ffffff;font-size:14px;\">For any inquiries, please contact us</p>" +
"<p style=\"margin:0;color:#ffffff;font-size:13px;opacity:0.9;\">📞 +82-1588-1234 | ✉️ info@luxuryhotel.com</p>" +
"<p style=\"margin:15px 0 0 0;color:#ffffff;font-size:12px;opacity:0.8;\">© 2025 LUXURY HOTEL. All rights reserved.</p>" +
"</td></tr>" +

"</table></td></tr></table></body></html>";
    }
    
    /**
     * 멤버십 등급에 따른 배지 색상 반환
     */
    private String getMembershipColor(String level) {
        if (level == null) return "#7AB2B2";
        
        switch (level.toUpperCase()) {
            case "SILVER":
            case "실버":
                return "#95a5a6";
            case "GOLD":
            case "골드":
                return "#f39c12";
            case "PLATINUM":
            case "플래티넘":
                return "#2c3e50";
            default:
                return "#7AB2B2"; // 프로젝트 색상
        }
    }
}