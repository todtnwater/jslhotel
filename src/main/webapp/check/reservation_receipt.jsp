<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<script>
    function printReceipt() {
        window.print();
    }
    
    function goCheckPage(gubun) { 
        const managerLevel = '${sessionScope.sessionLevel}';
        const form = document.forms['work3']; // 폼 이름 'work3' (오타 수정됨)

        if (form) {
            // ⭐⭐⭐ 매니저 레벨('top')이고 'managerList' 요청일 경우, Manager 서블릿으로 전환 ⭐⭐⭐
            if (managerLevel === 'top' && gubun === 'managerList') {
                form.t_gubun.value = "bookList"; // Manager.java에 있는 'bookList'로 설정
                form.action = "Manager";        // Manager 서블릿으로 요청
            } else {
                // 일반적인 사용자 조회 요청 ('checkPage', 'viewDetail' 등)
                form.t_gubun.value = gubun || "checkPage"; 
                form.action = "Check";          // Check 서블릿 유지
            }
            form.method = "post";
            form.submit();
        }
    }
</script>

<form name="work3">
    <input type="hidden" name="t_gubun">
</form>

<link rel="stylesheet" href="style/check.css">

<c:set var="reservation" value="${requestScope.reservation}" />
<c:set var="error" value="${requestScope.error}" />

<c:if test="${not empty reservation.rv_check_in_date and not empty reservation.rv_check_out_date}">
    <c:set var="checkInParts" value="${fn:split(reservation.rv_check_in_date, '-')}" />
    <c:set var="checkOutParts" value="${fn:split(reservation.rv_check_out_date, '-')}" />
    
    <fmt:parseDate value="${reservation.rv_check_in_date}" pattern="yyyy-MM-dd" var="checkInDate" />
    <fmt:parseDate value="${reservation.rv_check_out_date}" pattern="yyyy-MM-dd" var="checkOutDate" />
    
    <c:set var="checkInTime" value="${checkInDate.time}" />
    <c:set var="checkOutTime" value="${checkOutDate.time}" />
    <c:set var="millisPerDay" value="86400000" />
    <c:set var="nights" value="${(checkOutTime - checkInTime) / millisPerDay}" />
</c:if>

<div class="container">
    <c:choose>
        <c:when test="${not empty error}">
            <div class="content-area error-message">
                <p>${error}</p>
                <button onclick="goCheckPage()" class="btn btn-secondary">← 예약 조회 페이지로</button>
            </div>
        </c:when>
        <c:otherwise>
            <div class="header">
                <h1>예약 완료 (영수증)</h1>
                <p>예약이 성공적으로 완료되었습니다.</p>
            </div>
            
            <div class="content-area">
                
                <div class="section">
                    <h3>✅ 예약 정보</h3>
                    <table class="info-table">
                        <tr>
                            <th>예약번호</th>
                            <td>${reservation.rv_no}</td>
                        </tr>
                        <tr>
                            <th>객실 타입</th>
                            <td>${reservation.rv_room_type} (${reservation.rv_room_no})</td>
                        </tr>
                        <tr>
                            <th>체크인 / 아웃</th>
                            <td>
                                <strong>${reservation.rv_check_in_date}</strong> ~ 
                                <strong>${reservation.rv_check_out_date}</strong> 
                                <c:if test="${not empty nights}">
                                    (<fmt:formatNumber value="${nights}" pattern="0"/>박)
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <th>투숙 인원</th>
                            <td>${reservation.rv_guest_count}명</td>
                        </tr>
                        <tr>
                            <th>예약 등록일</th>
                            <td>${reservation.rv_register_date}</td>
                        </tr>
                    </table>
                </div>
                
                <div class="section">
                    <h3>👤 예약자 정보</h3>
                    <table class="info-table">
                        <tr>
                            <th>이름</th>
                            <td>${reservation.rv_customer_full_name}</td>
                        </tr>
                        <tr>
                            <th>이메일</th>
                            <td>${reservation.rv_customer_email}</td>
                        </tr>
                        <tr>
                            <th>연락처</th>
                            <td>${reservation.rv_customer_mobile_phone}</td>
                        </tr>
                    </table>
                </div>
                
                <div class="section">
                    <h3>💰 최종 결제 내역</h3>
                    <table class="info-table price-summary">
                        <tr>
                            <th>객실 원가</th>
                            <td><fmt:formatNumber value="${reservation.rv_original_price}" pattern="#,###"/>원</td>
                        </tr>
                        <tr>
                            <th style="color: #F94C66;">할인/사용 포인트</th>
                            <td style="color: #F94C66;">- <fmt:formatNumber value="${reservation.rv_discount_amount}" pattern="#,###"/>원</td>
                        </tr>
                        <tr class="final-price-row">
                            <th>최종 결제 금액</th>
                            <td><fmt:formatNumber value="${reservation.rv_total_price}" pattern="#,###"/>원</td>
                        </tr>
                    </table>
                </div>
                
                <c:if test="${not empty reservation.rv_request_message}">
                    <div class="section">
                        <h3>📋 요청 사항</h3>
                        <div class="request-box">
                            <p>${reservation.rv_request_message}</p>
                        </div>
                    </div>
                </c:if>
                <!-- 
                <div class="guide-section">
                    <h3>📌 체크인 안내</h3>
                    <ul>
                        <li>체크인: <strong>15:00</strong>부터 가능합니다.</li>
                        <li>체크아웃: <strong>11:00</strong>까지 완료해 주세요.</li>
                        <li>신분증(여권, 운전면허증 등)을 지참해 주세요.</li>
                        <li>예약 번호를 프론트에서 말씀해 주세요.</li>
                        <li>주차는 <strong>무료</strong>로 제공됩니다.</li>
                    </ul>
                </div>
                 -->
                <div class="footer">
                    <p>문의사항이 있으시면 언제든지 연락주세요</p>
                    <p style="opacity:0.9;">📞 1588-1234 | ✉️ support@jslhotel.com</p>
                    <p>© 2025 JSL Hotel. All rights reserved.</p>
                </div>
                
            </div>
            
            <div class="button-area">
                <div class="btn-group">
                    <button onclick="printReceipt()" class="btn btn-primary">🖨️ 인쇄하기</button>
                    <c:if test="${sessionLevel eq 'top'}">
                        <button onclick="goCheckPage('managerList')" class="btn btn-danger">
                            예약관리
                        </button>
                    </c:if>
                    
                    <button onclick="goCheckPage()" class="btn btn-secondary">← 뒤로가기</button>
                    <a href="Index" class="btn btn-secondary">🏠 메인으로</a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
