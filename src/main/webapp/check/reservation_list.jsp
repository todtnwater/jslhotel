<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file = "../common_header.jsp" %>
<%@ include file = "../common/menu_check.jsp" %>

<link rel="stylesheet" href="style/check.css">

<c:set var="sessionId" value="${sessionScope.sessionId}" />
<c:set var="sessionName" value="${sessionScope.sessionName}" />

<script>
    // 폼 이름 오타 수정 (wrok2 -> work2)
    function goViewDetail(rvNo) {
        const form = document.forms['work2']; 
        form.t_gubun.value = "viewDetail";
        form.rvNo.value = rvNo;
        form.method = "post";
        form.action = "Check";
        form.submit();
    }
    
    // 폼 이름 오타 수정 (wrok2 -> work2)
    function goCheckPage() {
        const form = document.forms['work2'];
        form.t_gubun.value = "checkPage";
        form.method = "post";
        form.action = "Check";
        form.submit();
    }
</script>

<form name="work2"> 
    <input type="hidden" name="t_gubun">
    <input type="hidden" name="rvNo">
</form>

<div class="container">
    <div class="header">
        <h1>📋 예약 조회 결과</h1>
        <c:choose>
            <c:when test="${not empty sessionId}">
                <p>${sessionId}님의 예약 내역입니다.</p>
            </c:when>
            <c:otherwise>
                <p>예약 정보를 확인하세요.</p>
            </c:otherwise>
        </c:choose>
    </div>
    
    <c:choose>
        <c:when test="${not empty error}">
            <div class="error-box">
                <p>⚠️ ${error}</p>
            </div>
            <div style="text-align: center; margin-top: 20px;">
                <button onclick="goCheckPage()" class="btn-back">← 예약 조회 페이지로</button>
            </div>
        </c:when>
        <c:when test="${empty reservationList}">
            <div class="empty-box">
                <div class="icon">📭</div>
                <h2>조회된 예약 내역이 없습니다</h2>
                <p>새로운 예약을 진행하시겠어요?</p>
                <div style="margin-top: 30px;">
                    <button onclick="goCheckPage()" class="btn-back">← 예약 조회 페이지로</button>
                    <a href="Index" class="btn-back" style="margin-left: 10px;">🏠 메인으로</a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="post-list">
                <c:forEach var="rv" items="${reservationList}">
                    <c:set var="statusClass" value="normal" />
                    <c:set var="statusText" value="예약 확인" />
                    <c:set var="statusBadgeClass" value="normal" />
                    
                    <c:choose>
                        <c:when test="${rv.rv_status eq 'CONFIRMED'}">
                            <c:set var="statusClass" value="notice" />
                            <c:set var="statusText" value="예약 확정" />
                            <c:set var="statusBadgeClass" value="notice" />
                        </c:when>
                        <c:when test="${rv.rv_status eq 'COMPLETED'}">
                            <c:set var="statusClass" value="popup" />
                            <c:set var="statusText" value="예약 완료" />
                            <c:set var="statusBadgeClass" value="popup" />
                        </c:when>
                        <c:when test="${rv.rv_status eq 'CANCELED'}">
                            <c:set var="statusClass" value="urgent" />
                            <c:set var="statusText" value="예약 취소" />
                            <c:set var="statusBadgeClass" value="urgent" />
                        </c:when>
                    </c:choose>
                    
                    <div class="post-card ${statusClass}" onclick="goViewDetail('${rv.rv_no}')" style="cursor: pointer;">
                        <div>
                            <span class="post-badge ${statusBadgeClass}">${statusText}</span>
                            <span class="priority-badge normal">${rv.rv_room_type}</span>
                        </div>
                        
                        <h3>
                            🏨 ${rv.rv_room_type} (${rv.rv_room_no})
                        </h3>
                        
                        <div class="post-preview">
                            📅 체크인: <strong>${rv.rv_check_in_date}</strong> ~ 체크아웃: <strong>${rv.rv_check_out_date}</strong><br>
                            👥 투숙 인원: ${rv.rv_guest_count}명 |
                            💰 결제 금액: <strong style="color: #4D869C; font-size: 1.1em;"><fmt:formatNumber value="${rv.rv_total_price}" pattern="#,###"/>원</strong>
                        </div>
                        
                        <div class="post-meta">
                            <div class="post-author">
                                예약번호: ${rv.rv_no}
                            </div>
                            <div class="post-stats">
                                <span>📅 ${rv.rv_register_date}</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <div style="text-align: center; margin-top: 30px;">
                <button onclick="goCheckPage()" class="btn-back">← 예약 조회 페이지로</button>
                <a href="Index" class="btn-back" style="margin-left: 10px;">🏠 메인으로</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>
